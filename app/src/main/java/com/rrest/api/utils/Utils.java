package com.rrest.api.utils;
public class Utils {
    public static final String BASE_URL = /*"http://dummy.restapiexample.com/api/v1/"*/
            "http://my-json-server.typicode.com/devMohaned/dummy-employee-api/";
    /* TODO (1): Retrofit requires good structure of backend, which means that it requires the json recieved to
         be the same as the one to be converted in the app, thus this API is not well structured enough
         where, it has status json object(status) that ruins the conversion of the json in this app
         (Volley would work just fine at this case, but unfortunately I made the volley app, so i got to stick with
         Retrofit), eventually, I had to convert to github hosting and host the perfect json to make sure it's
         structured correctly as it supposed to be, The app includes ability to GET/POST/PUT/DELETE, but
         using restapiexample.com doesn't work, so if you want to try it, change the api to a working one
         (highly structured).*/
}
