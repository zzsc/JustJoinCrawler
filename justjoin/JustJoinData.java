package net.elenx.epomis.provider.pl.justjoin;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class JustJoinData
{
    @Key
    public String title;
    @Key(value = "company_name")
    public String companyName;
    @Key
    public String city;
    @Key
    public String id;

}
