package ir.snapppay.paymentgateway.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import ir.snapppay.paymentgateway.util.Serializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jooq.JSON;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static ir.snapppay.paymentgateway.models.Tables.CLIENT;
import static ir.snapppay.paymentgateway.models.Tables.CLIENT_CONFIG;

@Slf4j
@Getter
@Setter
@ToString
public class ClientDetails {

    public static final Mapper MAPPER = new Mapper();

    /**
     * Represents the code of client.
     */
    private String code;

    /**
     * Represents the name of client.
     */
    private String name;

    /**
     * Represents the merchant code which is the owner of this client.
     */
    private String merchantCode;

    private long latestConfigId;

    private int paymentLinkDailyLimit;

    /**
     * Represents the client account identifier in our ledger.
     */
    private String ledgerAccount;

    /**
     * Represents the payment facilitator details in Json string format.
     */
    private String paymentFacilitatorDetails;

    /**
     * Indicates whether the client is active?
     */
    private Boolean isActive;

    /**
     * Represents the mobile number of the client.
     * It will be used to notify the client if purchase verification has failed.
     */
    private String notifyMobileNumber;

    /**
     * Whether the sms notification should send to client's mobile number.
     */
    private Boolean notifyWithSms;

    /**
     * Whether early settlement is enabled for the client?
     */
    private Boolean earlySettlement;

    /**
     * The client code to be settled from.
     */
    private String sourceCreditClientCode;

    /**
     * Max permitted amount for this client.
     */
    private Long maxPermittedAmount;

    /**
     * Encapsulates the transactions fee configuration.
     */
    private String transactionFeeConfig;

    /**
     * Represents the IP whitelist.
     */
    @Nullable
    private List<String> ipWhiteList;

    /**
     * Represents the client creation date.
     */
    private Instant createdAt;

    /**
     * Represents the client modification date.
     */
    private Instant modifiedAt;

    public List<String> getIpWhiteList() {
        if (ipWhiteList == null)
            ipWhiteList = Collections.emptyList();

        return ipWhiteList;
    }

    /**
     * jooQ mapper to convert jooQ records to {@link ClientDetails}.
     */
    private static class Mapper implements RecordMapper<Record, ClientDetails> {
        private final Serializer serializer = Serializer.newInstance();

        /**
         * @see RecordMapper#map(Record)
         */
        @Override
        public ClientDetails map(Record record) {
            var details = new ClientDetails();
            details.code                      = record.get(CLIENT.CODE);
            details.name                      = record.get(CLIENT.NAME);
            details.merchantCode              = record.get(CLIENT.MERCHANT_CODE);
            details.latestConfigId            = record.get(CLIENT_CONFIG.ID);
            details.ledgerAccount             = record.get(CLIENT.LEDGER_ACCOUNT);
            details.paymentFacilitatorDetails = record.get(CLIENT.PAYMENT_FACILITATOR_DETAILS);
            details.isActive                  = record.get(CLIENT.IS_ACTIVE);
            details.notifyMobileNumber        = record.get(CLIENT_CONFIG.NOTIFY_MOBILE_NUMBER);
            details.notifyWithSms             = record.get(CLIENT_CONFIG.NOTIFY_WITH_SMS);
            details.transactionFeeConfig      = record.get(CLIENT_CONFIG.TRANSACTION_FEE_CONFIG);
            details.createdAt                 = record.get(CLIENT.CREATED_AT);
            details.modifiedAt                = record.get(CLIENT.MODIFIED_AT);

            var ipWhiteListField = "ip_white_list";
            if (record.indexOf(ipWhiteListField) >= 0) {
                var ipWhiteList = (JSON) record.get(ipWhiteListField);
                if (ipWhiteList != null && StringUtils.hasText(ipWhiteList.data()))
                    details.ipWhiteList = serializer.fromJson(ipWhiteList.data(), new TypeReference<>() {});
            }

            return details;
        }
    }
}