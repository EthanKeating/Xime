package club.mcgamer.xime.profile.data.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class ReplyData {

    private UUID replyUUID;
    private long replyTimestamp;

    public boolean isActive() {
        return replyUUID != null && System.currentTimeMillis() - replyTimestamp < (120 * 1000);
    }

}
