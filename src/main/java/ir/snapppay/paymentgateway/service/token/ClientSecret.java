package ir.snapppay.paymentgateway.service.token;

import ir.snapppay.paymentgateway.util.Serializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static ir.snapppay.paymentgateway.models.Tables.CLIENT;

@Getter
@Setter
@ToString
public class ClientSecret {

    public static final Mapper MAPPER = new Mapper();

    /**
     * Represents the code of client.
     */
    private String code;

    /**
     * Represents the client secret key.
     */
    private String secretKey;

    /**
     * Indicates whether the client is active?
     */
    private Boolean isActive;

    /**
     * jooQ mapper to convert jooQ records to {@link ClientSecret}.
     */
    private static class Mapper implements RecordMapper<Record, ClientSecret> {
        private final Serializer serializer = Serializer.newInstance();

        /**
         * @see RecordMapper#map(Record)
         */
        @Override
        public ClientSecret map(Record record) {
            var clientSecret = new ClientSecret();
            clientSecret.code = record.get(CLIENT.CODE);
            clientSecret.secretKey = record.get(CLIENT.SECRET_KEY);
            clientSecret.isActive = record.get(CLIENT.IS_ACTIVE);

            return clientSecret;
        }
    }
}
