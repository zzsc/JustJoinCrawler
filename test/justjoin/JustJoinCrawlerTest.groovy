package net.elenx.epomis.provider.pl.justjoin

import com.google.gson.Gson
import net.elenx.epomis.entity.JobOffer
import net.elenx.epomis.service.connection5.ConnectionResponse5
import net.elenx.epomis.service.connection5.ConnectionService5
import org.apache.commons.io.IOUtils
import spock.lang.Specification

class JustJoinCrawlerTest extends Specification {
    void "extract JobOffers"() {
        given:
        ConnectionService5 connectionService5 = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        connectionService5.submit(_) >> connectionResponse5

        InputStream inputStream = JustJoinCrawlerTest.class.getResourceAsStream("justjoin.json")
        JustJoinData[] justJoinData = new Gson().fromJson(IOUtils.toString(inputStream, "UTF-8"), JustJoinData[].class)

        connectionResponse5.getResponseAsJson(_) >> Optional.of(justJoinData)

        JustJoinCrawler justJoinCrawler = new JustJoinCrawler(connectionService5)

        when:
        Set<JobOffer> offersSet = justJoinCrawler.offers()

        then:
        offersSet.size() == 29
    }
}
