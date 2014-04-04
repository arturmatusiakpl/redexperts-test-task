package pl2.lines.example.redexperts.utils;

import org.json.JSONException;
import org.json.JSONObject;

import pl2.lines.example.redexperts.model.Pin;

public class Json {

  private static final String LATITUDE  = "latitude";
  private static final String LONGITUDE = "longitude";
  private static final String LOCATION  = "location";
  private static final String TEXT      = "text";
  private static final String IMAGE      = "image";

  private Pin pin;
  private JSON_ERROR err = JSON_ERROR.ER_OK;

  public enum JSON_ERROR {
    ER_OK, JSON
  };
  
  public Json(){
    pin = new Pin();
  }
  
  public void parse(String json) {
    try {
      JSONObject jsonObj = new JSONObject(json);
      if (jsonObj.has(LOCATION)) {
        JSONObject jsonObjLocation = jsonObj.getJSONObject(LOCATION);
        
        if(jsonObjLocation.has(LATITUDE))
          pin.location.latitude = jsonObjLocation.getDouble(LATITUDE);
        
        if(jsonObjLocation.has(LONGITUDE))
          pin.location.longitude = jsonObjLocation.getDouble(LONGITUDE);      
      }
      
      if(jsonObj.has(TEXT))
        pin.text = jsonObj.getString(TEXT);
      
      if(jsonObj.has(IMAGE))
        pin.image = jsonObj.getString(IMAGE);
    } catch (JSONException e) {
      err = JSON_ERROR.JSON;
    }
  }
  
  public Pin getPin(){
    return pin;
  }
  
  public JSON_ERROR getError(){
    return err;
  }
  
  
}
