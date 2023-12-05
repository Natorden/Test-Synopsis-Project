package MyTunes.be;

public class SongOption {
    private final String option;
    private double valueInt;
    private String valueString;

    public SongOption(String option, double value){
        this.option=option;
        this.valueInt=value;
    }
    public SongOption(String option, String value){
        this.option=option;
        this.valueString=value;
    }

    public String getOption() {
        return option;
    }

    public double getValueInt() {
        return valueInt;
    }
    public String getValueString() {
        return valueString;
    }
}
