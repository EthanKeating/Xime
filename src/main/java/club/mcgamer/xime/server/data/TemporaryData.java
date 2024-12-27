package club.mcgamer.xime.server.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class TemporaryData {

    public static TemporaryData DEFAULT = new TemporaryData() {};

}
