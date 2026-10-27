package quickbite.boundary.gateway;

import org.springframework.stereotype.Component;
import quickbite.controller.exception.PaymentFailedException;
import java.util.UUID;

@Component
class MockPaymentGateway implements PaymentGatewayAPI {
    @Override
    public String mintaTransaksiPembayaran(double total, String tokenKartu) {
        if ("invalid_card".equalsIgnoreCase(tokenKartu)) {
            throw new PaymentFailedException("Gateway menolak transaksi");
        }
        return "PG-REF-" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase();
    }
}