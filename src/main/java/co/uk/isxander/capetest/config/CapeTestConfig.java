package co.uk.isxander.capetest.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CapeTestConfig extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            name = "Enabled",
            category = "General"
    )
    public boolean enabled;

    public CapeTestConfig(@NotNull File file) {
        super(file);
        initialize();
    }

}
