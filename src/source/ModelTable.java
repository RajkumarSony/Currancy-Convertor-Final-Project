
package source;

/**
 *
 * @author Rajkumar
 */
public class ModelTable {
    String from, to, date;
    double amount, result;
    
    public ModelTable(String from, String to, double amount, double result, String date){
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.result = result;
        this.date = date;
    }
    
    public String getFrom(){
        return from;
    }
    public String getTo(){
        return to;
    }
    public Double getAmount(){
        return amount;
    }
    public Double getResult(){
        return result;
    }
    public String getDate(){
        return date;
    }
    
    public void setFrom(String from){
        this.from = from;
    }
    public void setTo(String to){
        this.to = to;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public void setResult(double result){
        this.result = result;
    }
    public void setDate(String date){
        this.date = date;
    }
}
