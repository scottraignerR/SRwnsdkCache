package com.core.realwear.sdk.Util;

import com.core.realwear.sdk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */
public class WearLanguage {

    public String Name;
    public int ResourceId;
    public String ISO_Lang_Code;
    public Locale Android_Locale;

    public static List<WearLanguage> getCurrentLanguages(){
        List<WearLanguage> langs = new ArrayList<>();

        WearLanguage en = new WearLanguage();
        en.Name = "English";
        en.Android_Locale = Locale.ENGLISH;
        en.ResourceId = R.drawable.en;

        WearLanguage ch = new WearLanguage();
        ch.Name = "中国人的";
        ch.Android_Locale = Locale.CHINA;
        ch.ResourceId = R.drawable.ch;

        WearLanguage pr = new WearLanguage();
        pr.Name = "Português";
        pr.Android_Locale = new Locale("pt");
        pr.ResourceId = R.drawable.pt;

        Locale locale = Language.getLanguage();

        if(locale.getLanguage().equals(en.Android_Locale.getLanguage())) {
            langs.add(pr);
            langs.add(en);
            langs.add(ch);
        }else if(locale.getLanguage().equals(pr.Android_Locale.getLanguage())){
            langs.add(ch);
            langs.add(pr);
            langs.add(en);
        }else{
            langs.add(en);
            langs.add(ch);
            langs.add(pr);
        }

        return langs;
    }
}
