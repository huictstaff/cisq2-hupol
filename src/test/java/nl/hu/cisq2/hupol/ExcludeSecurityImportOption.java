package nl.hu.cisq2.hupol;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class ExcludeSecurityImportOption implements ImportOption {
    @Override
    public boolean includes(Location location) {
        return !location.contains("nl/hu/cisq2/hupol/security");
    }
}
