package com.github.kleesup.kleegdx.client.util.lang;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.kleesup.kleegdx.core.util.Verify;
import lombok.Getter;

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * A helper class caching all bundles for the same base file in {@link I18NBundle}.
 * Holds a {@link #getCurrent()} bundle which represents the main used currently.
 * Can save current bundle to system property via {@link #saveCurrent(String)} and then later be loaded with
 * {@link #setCurrentFromPropertyOrDefault(String, Locale)}.
 */
public class LangBundleCache {

    @Getter
    private I18NBundle current;
    private final FileHandle baseFile;
    private final ObjectMap<Locale, I18NBundle> bundles = new ObjectMap<>();
    public LangBundleCache(FileHandle baseFile, Locale... locales){
        Verify.nonNullArg(baseFile, "BaseFile cannot be null!");
        this.baseFile = baseFile;
        register(locales);
    }

    /**
     * Tries to register locales as bundles.
     * @param locales The locales to register.
     * @throws MissingResourceException If a bundle for a specified local doesn't exist.
     *                                  For reference, see {@link I18NBundle#createBundle(FileHandle)}.
     */
    public void register(Locale... locales){
        for(Locale locale : locales){
            if(locale == null)continue;
            bundles.put(locale, I18NBundle.createBundle(baseFile, locale));
        }
    }

    /**
     * Verifies if a locale is part of the loaded bundles.
     * @param locale The locale to check for.
     * @throws IllegalArgumentException When the locale is {@code null}.
     * @throws GdxRuntimeException When the bundle for that language isn't loaded.
     */
    private void verifyLoaded(Locale locale){
        Verify.nonNullArg(locale, "Locale cannot be null!");
        if(!bundles.containsKey(locale))throw new GdxRuntimeException("Locale "+locale+" is not loaded!");
    }

    /**
     * Sets the current set main bundle.
     * @param locale The locale to set the specific bundle.
     */
    public void setCurrent(Locale locale){
        verifyLoaded(locale);
        this.current = bundles.get(locale);
    }

    /**
     * Saves the current set bundle into system properties to be able to load it the next time.
     * @param propertyTag The system property tag to write to.
     */
    public void saveCurrent(String propertyTag){
        if(current != null)System.setProperty(propertyTag, current.getLocale().toLanguageTag());
    }

    /**
     * Will either set the current bundle to the one set in system properties or if there was non set, use a default
     * specified one.
     * @param propertyTag The system property tag to read from.
     * @param def The default value if there is non set in properties.
     */
    public void setCurrentFromPropertyOrDefault(String propertyTag, Locale def){
        String val = System.getProperty(propertyTag);
        if(val == null)setCurrent(def);
        else setCurrent(Locale.forLanguageTag(val));
    }

    /**
     * Receives a bundle for a specified locale.
     * @param locale The locale to get the bundle for.
     * @return The specific bundle.
     */
    public I18NBundle get(Locale locale){
        return bundles.get(locale);
    }



}
