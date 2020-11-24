
package source;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.System.exit;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import source.ModelTable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 *
 * @author Rajkumar
 */
public class HomeController implements Initializable {
    
    private Label label;
    @FXML
    private ComboBox<String> from_cmb;
    @FXML
    private ComboBox<String> to_cmb;
    @FXML
    private TextField input;
    @FXML
    private TextField output;
    
    Set rate, value;
    double amount;
    String date;
    
    Set<String> countryWithCodes = new TreeSet<String>();
    Set<String> country = new TreeSet<String>();
    
    @FXML
    private TableView<ModelTable> table_view;
    @FXML
    private TableColumn<ModelTable, String> from_tbl;
    @FXML
    private TableColumn<ModelTable, String> to_tbl;
    @FXML
    private TableColumn<ModelTable, Double> amount_tbl;
    @FXML
    private TableColumn<ModelTable, Double> result_tbl;
    @FXML
    private TableColumn<ModelTable, String> date_tbl;
    
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    
    //--------------# getResult() #--------------------
    
    @FXML
    private void getResult(ActionEvent event) {
        try{
            String str1 = from_cmb.getSelectionModel().getSelectedItem().toString();
            String str2 = to_cmb.getSelectionModel().getSelectedItem().toString();
            double val1 = Double.parseDouble(fromCompare(str1));
            double val2 = Double.parseDouble(toCompare(str2));
            try{
                amount = Double.parseDouble(input.getText());
            }
            catch(Exception e){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Sorry!, Invalid Input.");
                alert.setContentText("Please, Enter valid currancy value. Thank You!");
                alert.showAndWait();
            }
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            
            double base = amount / val1;
            double result = Double.parseDouble(df.format(base * val2));
            System.out.println(base * val2);
            output.setText(""+result);
            
            oblist.add(new ModelTable(str1,str2,amount,result,date));
            
            from_tbl.setCellValueFactory(new PropertyValueFactory<>("from"));
            to_tbl.setCellValueFactory(new PropertyValueFactory<>("to"));
            amount_tbl.setCellValueFactory(new PropertyValueFactory<>("amount"));
            result_tbl.setCellValueFactory(new PropertyValueFactory<>("result"));
            date_tbl.setCellValueFactory(new PropertyValueFactory<>("date"));
            table_view.setItems(oblist);
        }
        catch(Exception e){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Invalid country code.");
            alert.setContentText("Please, select valid Country Codes. Thank You!");
            alert.showAndWait();
            System.out.println(e);
        }
    }
    
    //--------------# fromCompare() #--------------------
    
    public String fromCompare(String str){
        String substr = str.substring(0, 3);
        Iterator k = rate.iterator();
        while(k.hasNext()){
            Map.Entry entry = (Map.Entry)k.next();
            
            if(substr.equals(entry.getKey())){
                System.out.println("matched!, "+str+", "+entry.getKey()+" = "+entry.getValue()); 
                String value = String.valueOf(entry.getValue());
                return value;
            }
        }
        return null;
    }
    
    //--------------# toCompare() #--------------------
    
    public String toCompare(String str){
        String substr = str.substring(0, 3);
        Iterator k = rate.iterator();
        while(k.hasNext()){
            Map.Entry entry = (Map.Entry)k.next();
            
            if(substr.equals(entry.getKey())){
                System.out.println("matched!, "+str+", "+entry.getKey()+" = "+entry.getValue()); 
                String value = String.valueOf(entry.getValue());
                return value;
            }
        }
        return null;
    }
    
    //--------------# getAPI() #--------------------
    
    public void getAPI(){
        String url_str = "http://data.fixer.io/api/latest?access_key=<Your-API-Key>";

        URL url;
        HttpURLConnection request = null;
        InputStream inputStream = null;
        try {
            url = new URL(url_str);
            request = (HttpURLConnection) url.openConnection();
            request.connect();
            inputStream = (InputStream) request.getContent();
            
        } catch (MalformedURLException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Unable to connect.");
            alert.setContentText("Please, Check your internet connection. Thank You!");
            alert.showAndWait();
            exit(0);
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Data not Found.");
            alert.setContentText("Please, Check your internet connection. Thank You!");
            alert.showAndWait();
            exit(0);
        }
        JSONParser jp = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject)jp.parse(new InputStreamReader(inputStream, "UTF-8"));
            
        } catch (UnsupportedEncodingException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Library files not found.");
            alert.setContentText("Please, Import 'json.simple-1.1.1.jar' library file. Thank You!");
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Unable to import library files.");
            alert.setContentText("Please, Check availability of library files. Thank You!");
            alert.showAndWait();
        } catch (ParseException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Sorry!, Unable to covert in json format.");
            alert.setContentText("Please, Check availability of library files. Thank You!");
            alert.showAndWait();
        }
        
        Map rates = (HashMap) jsonObject.get("rates");
        String base = (String) jsonObject.get("base");
        date = (String) jsonObject.get("date");
        rate = rates.entrySet();
        System.out.println(date);
        
        Iterator i = rate.iterator();
        while(i.hasNext()){
            Map.Entry entry = (Map.Entry)i.next();
              String c = (String) entry.getKey();
              country.add(c);
        //      System.out.println("Country Code = "+entry.getKey()+", Currancy Rate = "+v);
        }
        setNameWithCode();
    }
    
    //--------------# setNameWithCode() #--------------------
    
    public void setNameWithCode(){
        countryWithCodes.add("FJD (Fijian dollar)");
        countryWithCodes.add("MXN (Mexican peso)");
        countryWithCodes.add("STD (Sao Tome/Principe/Old Dobra)");
        countryWithCodes.add("LVL (latvian lats)");
        countryWithCodes.add("SCR (Seychelles Rupee)");
        countryWithCodes.add("CDF (Congolese Franc)");
        countryWithCodes.add("BBD (Barbados Doolar)");
        countryWithCodes.add("GTQ (Guatemalan Quetzal)");
        countryWithCodes.add("CLP (Chilean Peso)");
        countryWithCodes.add("HNL (Honduran Lempira)");
        countryWithCodes.add("UGX (Uganda Shilling)");
        countryWithCodes.add("ZAR (south African Rand)");
        countryWithCodes.add("TND (Tunisian Dinar)");
        countryWithCodes.add("CUC (Cuban Convertible peso)");
        countryWithCodes.add("BSD (Bahamian Dollar)");
        countryWithCodes.add("SLL (Sierra leone leone)");
        countryWithCodes.add("SDG (Sudanese pound)");
        countryWithCodes.add("IQD (Iraqi Dinar)");
        countryWithCodes.add("CUP (cuban Peso)");
        countryWithCodes.add("GMD (Gambian Dalasi)");
        countryWithCodes.add("TWD (Talwan Dollar)");
        countryWithCodes.add("RSD (serbian Dinar)");
        countryWithCodes.add("DOP (Dominican R.peso)");
        countryWithCodes.add("KMF (Comoros Franc)");
        countryWithCodes.add("MYR (Malaysian Ringgit)");
        countryWithCodes.add("FKP (Falkland Islands Pound)");
        countryWithCodes.add("XOF (CFA Franc BCEAO)");
        countryWithCodes.add("GEL (Georgian lari)");
        countryWithCodes.add("BTC (Bitcoin)");
        countryWithCodes.add("UYU (Uruguayan peso)");
        countryWithCodes.add("MAD (Moroccan Dirham)");
        countryWithCodes.add("CVE (Cape Verde Escudo)");
        countryWithCodes.add("TOP (Tonga Pa'anga)");
        countryWithCodes.add("AZN (Azerbaijan New Manat)");
        countryWithCodes.add("OMR (Omani Riai)");
        countryWithCodes.add("PGK (Papua New Guinea Kina)");
        countryWithCodes.add("KES (kenyan Shilling)");
        countryWithCodes.add("SEK (Swedish krona)");
        countryWithCodes.add("BTN (Bhutan Ngultrum)");
        countryWithCodes.add("UAH (Ukraine Hryvnia)");
        countryWithCodes.add("GNF (Guinea Franc)");
        countryWithCodes.add("ERN (Eritrean Nakfa)");
        countryWithCodes.add("MZN (Mozambique New metical)");
        countryWithCodes.add("SVC (EL Salvador Colon)");
        countryWithCodes.add("ARS (Argentine peso)");
        countryWithCodes.add("QAR (Qatari Riai)");
        countryWithCodes.add("IRR (iranian Riai)");
        countryWithCodes.add("MRO (Mauritanian old Ouguiya)");
        countryWithCodes.add("CNY (Chinese Yuan Renminbi)");
        countryWithCodes.add("THB (Thai bath)");
        countryWithCodes.add("UZS (Uzbekistan Som)");
        countryWithCodes.add("XPF (CFP Franc)");
        countryWithCodes.add("BDT (bangladeshi Taka)");
        countryWithCodes.add("LYD (Libyan)");   
        countryWithCodes.add("BMD (Bermudian Dollar)");
        countryWithCodes.add("KWD (Kuwaiti Dinar ) ");   
        
        countryWithCodes.add("PHP (Philippine peso)");
        countryWithCodes.add("RUB (Russian Rouble)");
        countryWithCodes.add("PYG (Paraguay Guarani)");
        countryWithCodes.add("ISK (Iceland Krona)");
        countryWithCodes.add("JMD (jamaican Dollar)");
        countryWithCodes.add("COP (Colombian peso)");
        countryWithCodes.add("MKD (Macedonian Denar)");
        countryWithCodes.add("USD (US Dollar)");
        countryWithCodes.add("DZD (Algerian Dinar)");
        countryWithCodes.add("PAB (Panamanian Balboa)");
        countryWithCodes.add("GGP (Guernsey pound)");
        countryWithCodes.add("SGD (Singapore Dollar)");
        countryWithCodes.add("ETB (Ethiopian Birr)");
        countryWithCodes.add("JEP (Jersey pound)");
        countryWithCodes.add("KGS (Kyrgyzstanian Som)");
        countryWithCodes.add("SOS (Somali Shillinig)");
        countryWithCodes.add("VEF (Venezuelan Bolivar Fuerte)");
        countryWithCodes.add("VUV (Vanuata Vatu)");
        countryWithCodes.add("LAK (Lao Kip)");
        countryWithCodes.add("BND (Brunei Dollar)");
        countryWithCodes.add("ZMK (Zambian Kwacha)");
        countryWithCodes.add("XAF (CFA Franc BEAC)");
        countryWithCodes.add("LRD (Liberian Dollar)");
        countryWithCodes.add("XAG (Silver(oz.))");
        countryWithCodes.add("CHF (Swiss franc)");
        countryWithCodes.add("HRK (Croatian Kuna)");
        countryWithCodes.add("ALL (Albanian Lek)");
        countryWithCodes.add("DJF (Djibouti franc)");
        countryWithCodes.add("ZMW (Zambian Kwacha)");
        countryWithCodes.add("TZS (Tanzanian Shilling)");
        countryWithCodes.add("VND (Vietnamese Dong)");
        countryWithCodes.add("XAU (Gold(oz.))");
        countryWithCodes.add("AUD (Australian Dollar)");
        countryWithCodes.add("ILS (Israeli New Shekel) ");
        countryWithCodes.add("GHS (Ghananian New Cedi)");
        countryWithCodes.add("GYD (Guyanese Dollar)");
        countryWithCodes.add("KPW (North Korean Won)");
        countryWithCodes.add("BOB (Bolivian Boliviano)");
        countryWithCodes.add("KHR (Cambodian Riel)");
        countryWithCodes.add("MDL (Moldovan Leu)");
        countryWithCodes.add("IDR (Indonesian Rupiah)");
        countryWithCodes.add("KYD (Cayman Islands Dollar)");
        countryWithCodes.add("AMD (Armenian Dram)");
        countryWithCodes.add("BWP (Botswana Pula)");
        countryWithCodes.add("SHP (St. Helena Pound)");
        countryWithCodes.add("TRY (Turkish Lira)");
        countryWithCodes.add("LBP (Lebanese Pound)");
        countryWithCodes.add("TJS (Tajikstan Somoni)");
        countryWithCodes.add("JOD (Jordanian Dinar)");
        countryWithCodes.add("AED (Utd. Arab Emir. Dirham)");
        countryWithCodes.add("HKD (Hong Kong Dollar)");
        countryWithCodes.add("RWF (Rwandan Franc)");
        countryWithCodes.add("EUR (Euro)");

        countryWithCodes.add("DKK (Danish Krone)");
        countryWithCodes.add("CAD (Canadian Dollar)");
        countryWithCodes.add("BGN (Bulgarian Lev)");
        countryWithCodes.add("MMK (Myanmar Kyat)");
        countryWithCodes.add("MUR (Mauritius Rupee)");
        countryWithCodes.add("NOK (Norwegian Kroner)");
        countryWithCodes.add("SYP (Syrian Pound)");
        countryWithCodes.add("IMP (Isle of Mam Pound)");
        countryWithCodes.add("ZWL (Zimbabwe Dollar)");
        countryWithCodes.add("GIP (Gibraltar Pound)");
        countryWithCodes.add("RON (Romanian New Lei)");
        countryWithCodes.add("LKR (Sri Lanka Rupee)");
        countryWithCodes.add("NGN (Nigerian Naira)");
        countryWithCodes.add("CRC (Costa Rican Colon)");
        countryWithCodes.add("CZK (Czezh Koruna)");
        countryWithCodes.add("PKR (Pakistan rupee)");
        countryWithCodes.add("XCD (East Caribbean Dollar)");
        countryWithCodes.add("ANG (NL Antillian Guilder)");
        countryWithCodes.add("HTG (Haitian Gourde)");
        countryWithCodes.add("BHD (Bahraini Dinar)");
        countryWithCodes.add("KZT (KAzakhstan Tenge)");
        countryWithCodes.add("SRD (Suriname Dollar)");
        countryWithCodes.add("SZL (Swaziland Lilangeni)");
        countryWithCodes.add("LTL (Lithuanian Litas)");
        countryWithCodes.add("SAR (Saudi Riyal )");
        countryWithCodes.add("TTD (Trinidad /Tobago Dollar)");
        countryWithCodes.add("YER (yemeni RiaL)");
        countryWithCodes.add("MVR (Maldive Rufiyaa)");
        countryWithCodes.add("AFN (Afghanistan Afhani)");
        countryWithCodes.add("INR (Indian Rupee)");
        countryWithCodes.add("AWG (Aruban Florin)");
        countryWithCodes.add("KRW (South-Korean Won)");
        countryWithCodes.add("NPR (Nepalese Rupee)");
        countryWithCodes.add("JPY (Japanese Yen)");
        countryWithCodes.add("MNT (Mongolian Tugrik)");
        countryWithCodes.add("AOA (Angolan Kwanza)");
        countryWithCodes.add("PLN (Polish Zloty)");
        countryWithCodes.add("GBP (British pound)");
        countryWithCodes.add("SBD (Solomon Islands Dollar)");
        countryWithCodes.add("BYN (Belarusian Ruble)");
        countryWithCodes.add("HUF (Hungarian Forint)");
        countryWithCodes.add("BYR (Belarusian old Ruble)");
        countryWithCodes.add("BIF (Burundi Franc)");
        countryWithCodes.add("MWK (Malawi kwacha)");
        countryWithCodes.add("MGA (Malagasy Ariary)");
        countryWithCodes.add("XDR (International Monetory Fund Special Drawing Rights (SDR)))");
        countryWithCodes.add("BZD (Belize Doolar)");
        countryWithCodes.add("BAM (Bosnian Mark)");
        countryWithCodes.add("EGP (Egyptian pound)");
        countryWithCodes.add("MOP (Macau Pataca)");
        countryWithCodes.add("NAD (Namibia Dollar)");
        countryWithCodes.add("NIO (Nicaraguan Cordoba Ora)");
        countryWithCodes.add("PEN (Peruvian Nuevo Sol)");
        countryWithCodes.add("NZD (New Zealand Dollar)");
        countryWithCodes.add("WST (Samoan Tala)");
        countryWithCodes.add("TMT (Turkmenistan New Manat)");
        countryWithCodes.add("CLF (Chile pound)");
        countryWithCodes.add("BRL (Brazilian Real)");
    }
    
    //--------------# initialize() #--------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            getAPI();
            from_cmb.getItems().addAll(countryWithCodes);
            to_cmb.getItems().addAll(countryWithCodes);
        } catch (Exception ex) {
            System.out.print(""+ex);
        }
    }        
}
