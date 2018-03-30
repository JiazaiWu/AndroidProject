package com.example.jiazai.AirhockeyActivity.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TexrResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String nextline = null;

            while ((nextline = br.readLine()) != null) {
                body.append(nextline);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource " + resourceId, e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource can not be found " + resourceId, e);
        }

        return body.toString();
    }
}
