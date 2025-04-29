package com.github.kleesup.kleegdx.client.test;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.github.kleesup.kleegdx.client.asset.Asset;
import com.github.kleesup.kleegdx.client.asset.AssetLoader;
import com.github.kleesup.kleegdx.client.asset.json.JsonAsset;
import com.github.kleesup.kleegdx.client.asset.json.JsonAssetFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class JsonTest {

    public static void main(String[] args) {

        try {
            JsonAssetFactory factory = new JsonAssetFactory(null);
            URL url = JsonTest.class.getClassLoader().getResource("Person.json");
            File file = Paths.get(url.toURI()).toFile();
            Person person = factory.build(PersonConfig.class, new FileHandle(file), Person::new);
            System.out.println("Is loaded = "+(person != null));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Person extends JsonAsset<PersonConfig> {

        public Asset<Texture> image;

        public Person(PersonConfig personConfig) {
            super(personConfig);
        }

        @Override
        public void dispose() {

        }

        @Override
        public void load(AssetLoader loader) {
            // Now we could call:
            // image = loader.queueLoad(config.imagePath, Texture.class);
            System.out.println("Loading from "+config.toString());
            disposeConfig();
        }
    }

    public static class PersonConfig{

        String name;
        int age;
        double height;

        String imagePath;

        @Override
        public String toString() {
            return "PersonConfig{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", height=" + height +
                    ", imagePath='" + imagePath + '\'' +
                    '}';
        }
    }

}
