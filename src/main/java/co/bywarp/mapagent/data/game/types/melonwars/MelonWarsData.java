/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.melonwars;

import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.game.types.melonwars.blocks.BlueMelonDataBlock;
import co.bywarp.mapagent.data.game.types.melonwars.blocks.RedMelonDataBlock;
import co.bywarp.mapagent.data.game.types.melonwars.teams.BlueTeam;
import co.bywarp.mapagent.data.game.types.melonwars.teams.RedTeam;
import co.bywarp.mapagent.utils.DataUtils;

public class MelonWarsData extends GameData {

    public MelonWarsData() {
        super("Melon Wars",
                GameDataType.MELON_WARS,
                DataUtils.inlineList(
                        new RedTeam(),
                        new BlueTeam()
                ),
                new RedMelonDataBlock(),
                new BlueMelonDataBlock()
        );
    }

}
