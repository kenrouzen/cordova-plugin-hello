package com.example.plugin;

import java.util.HashMap;
import java.util.Map;


//import com.example.plugin.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import android.content.Intent;

public class Hello extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {

            if (data.length() != 1) {
                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                return false;
            }

            // Parse the data
            JSONObject obj = data.getJSONObject(0);

            JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
            Map<String, String> extrasMap = new HashMap<String, String>();

            // Populate the extras if any exist
            if (extras != null) {
                JSONArray extraNames = extras.names();
                for (int i = 0; i < extraNames.length(); i++) {
                    String key = extraNames.getString(i);
                    String value = extras.getString(key);
                    extrasMap.put(key, value);
                }
            }

            sendBroadcast(obj.getString("action"), extrasMap);
            //return new PluginResult(PluginResult.Status.OK);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            return true;

        } else {
            
            return false;

        }
    }
    
    void sendBroadcast(String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            intent.putExtra(key, value);
        }

        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        this.cordova.getActivity().sendBroadcast(intent);
    }
}
