package feup.cmov.cafeteriaadmin.models.voucher;


import com.orm.SugarRecord;

import feup.cmov.cafeteriaadmin.models.Order;

public abstract class Voucher extends SugarRecord{

    public String signature;
    public int type;
    public String serialNumber;
    public String uuid;

    public Voucher(String signature, String serialNumber, int type, String uuid)
    {
        this.signature = signature;
        this.type = type;
        this.serialNumber = serialNumber;
        this.uuid = uuid;
    }

    public Voucher() {
    }

    public abstract void apply(Order cart);
}
