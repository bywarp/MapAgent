/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.spleef;

import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.game.types.spleef.teams.PlayersTeam;
import co.bywarp.mapagent.utils.DataUtils;

public class SpleefData extends GameData {

    public SpleefData() {
        super("Spleef",
                GameDataType.SPLEEF,
                DataUtils.inlineList(
                        new PlayersTeam()
                )
        );
    }

}
