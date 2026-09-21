package de.daniel.bactromod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import de.daniel.bactromod.config.ConfigData;
import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {
    @Test
    void packagedTranslationsHaveMatchingKeysAndArguments() throws IOException {
        try (var jar = new JarFile(System.getProperty("bactromod.test.jar"))) {
            var english = translations(jar, "assets/bactromod/lang/en_us.json");
            for (var field : ConfigData.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(BooleanOption.class) || field.isAnnotationPresent(IntegerOption.class)) {
                    assertTrue(english.containsKey("bactromod.options." + field.getName()), field.getName());
                    assertTrue(english.containsKey("bactromod.options." + field.getName() + ".desc"), field.getName());
                }
            }
            var languages = jar.stream().map(entry -> entry.getName())
                    .filter(name -> name.startsWith("assets/bactromod/lang/") && name.endsWith(".json")).toList();
            assertFalse(languages.isEmpty());
            for (String language : languages) {
                var translated = translations(jar, language);
                assertEquals(english.keySet(), translated.keySet(), language);
                for (String key : english.keySet()) {
                    assertEquals(arguments(english.get(key)), arguments(translated.get(key)), language + ": " + key);
                }
            }
        }
    }

    @Test
    void packagedMetadataIsExpandedAndReferencesPackagedFiles() throws IOException {
        try (var jar = new JarFile(System.getProperty("bactromod.test.jar"))) {
            JsonObject metadata = json(jar, "fabric.mod.json");
            assertFalse(metadata.toString().contains("${"), "Unexpanded metadata placeholder");
            assertEquals(System.getProperty("bactromod.test.version"), metadata.get("version").getAsString());
            assertEquals("client", metadata.get("environment").getAsString());
            assertTrue(metadata.getAsJsonObject("depends").has("fabric-api"));
            assertTrue(metadata.getAsJsonObject("depends").get("fabric-api").getAsString().startsWith(">="));
            assertTrue(metadata.getAsJsonObject("depends").get("fabricloader").getAsString().startsWith(">="));
            assertTrue(metadata.getAsJsonObject("depends").get("minecraft").getAsString().startsWith("~"));
            assertFalse(metadata.getAsJsonObject("depends").has("modmenu"));
            assertTrue(metadata.getAsJsonObject("suggests").has("modmenu"));
            assertNotNull(jar.getJarEntry(metadata.get("icon").getAsString()));
            for (var entrypoints : metadata.getAsJsonObject("entrypoints").asMap().values()) {
                for (var entrypoint : entrypoints.getAsJsonArray()) {
                    assertClassPackaged(jar, entrypoint.getAsString());
                }
            }
            for (var config : metadata.getAsJsonArray("mixins")) {
                JsonObject mixins = json(jar, config.getAsString());
                String packageName = mixins.get("package").getAsString();
                for (var mixin : mixins.getAsJsonArray("client")) {
                    assertClassPackaged(jar, packageName + "." + mixin.getAsString());
                }
            }
            assertTrue(jar.stream().noneMatch(entry -> entry.getName().contains("gametest/")),
                    "Client test classes must not ship in the mod jar");
        }
    }

    private static void assertClassPackaged(JarFile jar, String name) {
        assertNotNull(jar.getJarEntry(name.replace('.', '/') + ".class"), name);
    }

    private static JsonObject json(JarFile jar, String name) throws IOException {
        assertNotNull(jar.getJarEntry(name), name);
        try (var reader = new JsonReader(new InputStreamReader(jar.getInputStream(jar.getJarEntry(name)), StandardCharsets.UTF_8))) {
            reader.setStrictness(Strictness.STRICT);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek(), name);
            return json;
        }
    }

    private static Map<String, String> translations(JarFile jar, String name) throws IOException {
        assertNotNull(jar.getJarEntry(name), name);
        Map<String, String> result = new HashMap<>();
        try (var reader = new JsonReader(new InputStreamReader(jar.getInputStream(jar.getJarEntry(name)), StandardCharsets.UTF_8))) {
            reader.setStrictness(Strictness.STRICT);
            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                assertEquals(JsonToken.STRING, reader.peek(), name + ": " + key);
                String value = reader.nextString();
                assertFalse(value.isBlank(), name + ": " + key);
                assertNull(result.put(key, value), name + ": duplicate key " + key);
            }
            reader.endObject();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek(), name);
        }
        return result;
    }

    private static Set<Integer> arguments(String text) {
        var matcher = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%])").matcher(text);
        Set<Integer> arguments = new HashSet<>();
        int implicitIndex = 1;
        while (matcher.find()) {
            if (matcher.group(2).equals("%")) continue;
            assertEquals("s", matcher.group(2), "Unsupported translation format: " + text);
            arguments.add(matcher.group(1) == null ? implicitIndex++ : Integer.parseInt(matcher.group(1)));
        }
        return arguments;
    }
}
