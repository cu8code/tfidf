import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

class App {

  public static HashMap<String, Float> getVal(String arg) throws IOException, JSONException {
    String jsonString = new String(Files.readAllBytes(Paths.get("dum.json")));

    JSONObject jsonObject = new JSONObject(jsonString);

    Iterator<String> keys = jsonObject.keys();

    HashMap<String,Float> ff = new HashMap<>();

    float IDF = idf(arg,jsonObject);

    while(keys.hasNext()){

        String file = keys.next();
        JSONObject e = jsonObject.getJSONObject(file);

        float TF = tf(arg,e);
        ff.put(file,TF * IDF);

    }

      List<Map.Entry<String,Float>> list = new LinkedList<>(ff.entrySet());
      Collections.sort(list, Map.Entry.comparingByValue());
      HashMap<String,Float> temp = new LinkedHashMap<>();
      for(Map.Entry<String,Float> aa : list){
          temp.put(aa.getKey(),aa.getValue());
      }

      return temp;
  }

  private static float tf(String s,JSONObject hm) throws JSONException {
    int sum = 1;
    Iterator<String> h = hm.keys();
    while(h.hasNext()){
        String key =  h.next();
//        System.out.println(hm.getInt(key));
      sum += hm.getInt(
       key
    );
//      System.out.println(sum);
    }
    float tf = 0;
    try {
          tf = ((float) (hm.getInt(s) + 1) / (float) sum);
      } catch (Error e){
           tf = 0;
      }
    return tf;
 }


 private static float idf(String s, JSONObject hm) throws JSONException {
      int cont = 1;
      int fc = 1;

      Iterator<String> it = hm.keys();
      while(it.hasNext()){
          fc += 1;
          String a = hm.get(it.next()).toString();
          JSONObject j = new JSONObject(a);
          Iterator<String> st = j.keys();
          while (st.hasNext()){
              String ss = st.next();
              if( ss == s){
                  cont += 1;
              }
          }
      }

      float res = (float) Math.log((float) fc / (float) cont);
     System.out.println(fc + " " + cont + " " + res );
        return res;
 }

  public static void main(String[] args) throws Exception {
      System.out.print(getVal("Play"));
//    if(args.length > 0){
//      if(Objects.equals(args[0], "serve")){
//        serve("./dum.json");
//      } else {
//        index(args[0]);
//      }
//    } else {
//      index("./");
//    }
  }

  public static void serve(String index) throws IOException {
    Server app  = new Server("localhost",8000);
  }

  public static void index(String path) throws java.lang.Exception {

      HashMap<String , HashMap<String,Integer>> map = new HashMap<>();

      // get all the files
      File folder;
      folder = new File(path);

      index_file(map, folder);
      System.out.println("Dumping json .......");
      JSONObject json = new JSONObject(map);

      try {
        FileWriter writer = new FileWriter("dum.json");
        writer.write(json.toString());
        writer.close();
      } catch (IOException e){
        e.printStackTrace();
      }
  }

  private static void index_file(HashMap<String, HashMap<String, Integer>> map, File folder) throws IOException {
    for (final File fileEntry : folder.listFiles()) {

      if (fileEntry.isFile()) {
        String fileName = fileEntry.getAbsoluteFile().getName();
        if(!fileName.endsWith(".txt")){
          System.out.println("Skipping " + fileEntry.getPath());
          continue;
        }

        System.out.println("indexing " + fileEntry.getPath() + " .......");

        // read the files
        HashMap<String,Integer> countMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(fileEntry.getAbsoluteFile()));

        try {
          String line = br.readLine();

          while (null != line) {
            Lexer t = new Lexer(line.toCharArray());
            for(char[] m : t){
              String s = new String(m);
              if (countMap.containsKey(s)){
                countMap.put(s, countMap.get(s) + 1);
              } else {
                countMap.put(s,1);
              }
            }
            line = br.readLine();
          }

        } finally {
          br.close();
        }

        // sort the countMap
        List<Map.Entry<String,Integer>> list = new LinkedList<>(countMap.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());
        HashMap<String,Integer> temp = new LinkedHashMap<>();
        for(Map.Entry<String,Integer> aa : list){
          temp.put(aa.getKey(),aa.getValue());
        }
        map.put(fileEntry.getPath(), temp);
      }
      else if (fileEntry.isDirectory()){
        index_file(map,fileEntry);
      }
    }
  }

}
