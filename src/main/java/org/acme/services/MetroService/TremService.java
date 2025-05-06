package org.acme.services.MetroService;

import org.acme.entities.Metro.Trem;

public class TremService {
    public boolean validateTrem(Trem trem) {
        if (trem == null)
            return false;
        if (trem.getLinha() == null)
            return false;

        return true;
    }
}
