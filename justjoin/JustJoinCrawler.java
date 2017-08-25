package net.elenx.epomis.provider.pl.justjoin;

import com.google.api.client.http.HttpMethods;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.SneakyThrows;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.provider.JobOfferProvider;
import net.elenx.epomis.service.connection5.ConnectionRequest5;
import net.elenx.epomis.service.connection5.ConnectionService5;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
public class JustJoinCrawler implements JobOfferProvider
{

    static String REST_URL = "http://justjoin.it/api/offers";
    ConnectionService5 connectionService5;

    @Override
    public Set<JobOffer> offers()
    {

        ConnectionRequest5 connectionRequest5 = ConnectionRequest5
            .builder()
            .url(REST_URL)
            .method(HttpMethods.GET)
            .build();

        return connectionService5
            .submit(connectionRequest5)
            .getResponseAsJson(JustJoinData[].class)
            .map(JustJoinData -> Arrays.stream(JustJoinData)
                                       .filter(this::hasJavaKeyWord)
                                       .map(this::extractOffer)
                                       .collect(Collectors.toSet())
                )
            .orElse(ImmutableSet.of());

    }

    @SneakyThrows
    private JobOffer extractOffer(JustJoinData justJoinData)
    {

        return JobOffer
            .builder()
            .title(justJoinData.getTitle())
            .company(justJoinData.getCompanyName())
            .location(justJoinData.getCity())
            .href("http://justjoin.it/offers/" + justJoinData.getId())
            .build();
    }

    public boolean hasJavaKeyWord(JustJoinData justJoinData)
    {
        return (justJoinData.getTitle().toLowerCase().contains("java "));
    }

}

