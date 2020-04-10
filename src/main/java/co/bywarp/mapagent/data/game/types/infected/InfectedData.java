/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.infected;

import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.game.types.infected.blocks.SupplyDropDataBlock;
import co.bywarp.mapagent.data.game.types.infected.teams.AliveTeam;
import co.bywarp.mapagent.data.game.types.infected.teams.UndeadTeam;
import co.bywarp.mapagent.utils.DataUtils;

public class InfectedData extends GameData {

    public InfectedData() {
        super("Infected",
                GameDataType.INFECTED,
                DataUtils.inlineList(
                        new AliveTeam(),
                        new UndeadTeam()
                ),
                new SupplyDropDataBlock());
    }

}
