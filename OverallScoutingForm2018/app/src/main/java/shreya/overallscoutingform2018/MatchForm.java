package shreya.overallscoutingform2018;

public class MatchForm extends Form {

    public static final class Items {
        public static final Item PRESENT = new Item(1, "Present", Item.Datatype.BOOLEAN);
        public static final Item CAN_CLIMB = new Item(7, "Can climb?", Item.Datatype.BOOLEAN);
        public static final Item COMMENTS = new Item(44, "Comments", Item.Datatype.STRING);
        public static final Item RATE_DRIVING = new Item(79, "Rate driving", Item.Datatype.INTEGER);
        public static final Item AUTO_CROSS_BASELINE = new Item(91, "Auto: Cross Baseline?", Item.Datatype.BOOLEAN);
        public static final Item CLIMB_SUCCESS = new Item(101, "Climb Success?", Item.Datatype.BOOLEAN);
        public static final Item STAYS_PUT_WHEN_POWER_CUT = new Item(102, "Stays Put When Power Cut?", Item.Datatype.BOOLEAN);
        public static final Item DID_THEY_BREAK_DOWN = new Item(104, "Did They Break Down?", Item.Datatype.BOOLEAN);

        public static final Item AUTO_CUBE_IN_VAULT = new Item(123, "Auto: Cube in Vault?", Item.Datatype.BOOLEAN);
        public static final Item AUTO_CUBE_IN_ALLY_SWITCH = new Item(124, "Auto: Cube in Ally Switch?", Item.Datatype.BOOLEAN);
        public static final Item AUTO_CUBE_IN_SCALE = new Item(125, "Auto: Cube in Scale?", Item.Datatype.BOOLEAN);
        public static final Item AUTO_CUBE_IN_OPPONENT_SWITCH = new Item(126, "Auto: Cube in Opponent Switch?", Item.Datatype.BOOLEAN);
        public static final Item CUBE_IN_VAULT = new Item(127, "Cube in Vault", Item.Datatype.INTEGER);
        public static final Item CUBE_IN_ALLY_SWITCH = new Item(128, "Cube in Ally Switch", Item.Datatype.INTEGER);
        public static final Item CUBE_IN_SCALE = new Item(129, "Cube in Scale", Item.Datatype.INTEGER);
        public static final Item CUBE_IN_OPPONENT_SWITCH = new Item(130, "Cube in Opponent Switch", Item.Datatype.INTEGER);
        public static final Item CUBE_DROPPER = new Item(131, "Cube Dropper", Item.Datatype.INTEGER);
        public static final Item HELP_OTHERS_CLIMB = new Item(132, "Help Others Climb?", Item.Datatype.BOOLEAN);
        public static final Item PLAY_DEFENSE = new Item(133, "Play Defense?", Item.Datatype.BOOLEAN);
        public static final Item RATE_DEFENSE = new Item(134, "Rate Defense", Item.Datatype.INTEGER);
    } // End Items

    public static final Item[] matchItems = {
            Items.AUTO_CROSS_BASELINE, Items.AUTO_CUBE_IN_VAULT,
            Items.AUTO_CUBE_IN_ALLY_SWITCH, Items.AUTO_CUBE_IN_SCALE,
            Items.AUTO_CUBE_IN_OPPONENT_SWITCH, Items.CUBE_IN_VAULT,
            Items.CUBE_IN_ALLY_SWITCH, Items.CUBE_IN_SCALE,
            Items.CAN_CLIMB, Items.CLIMB_SUCCESS, Items.CUBE_IN_OPPONENT_SWITCH,
            Items.COMMENTS, Items.DID_THEY_BREAK_DOWN, Items.CUBE_DROPPER,
            Items.PLAY_DEFENSE, Items.HELP_OTHERS_CLIMB,
            Items.RATE_DEFENSE,
            Items.PRESENT, Items.RATE_DRIVING, Items.STAYS_PUT_WHEN_POWER_CUT
    }; // End matchItems



    public MatchForm(int tabletNum, int teamNum, int matchNum, String scoutName) {
        super(FormType.MATCH_FORM, tabletNum, teamNum, matchNum, scoutName);
    } // End constructor

    public MatchForm(int reportID, int tabletNum, int teamNum, int matchNum, String scoutName) {
        super(reportID, FormType.MATCH_FORM, tabletNum, teamNum, matchNum, scoutName);
    } // End constructor

    public MatchForm(String rawForm) {
        super(rawForm);
    } // End constructor

    public static String averageFormVisualizer(String rawData) {
        String visualizedForm = "";
        String[] rawDataParts = rawData.split("\\##");
        String averages = rawDataParts[0];
        String proportions = rawDataParts[1];

        String[] itemAvgs = averages.split("\\" + Form.ITEM_DELIMITER);
        for (String itemAvg : itemAvgs) {
            String[] avgParts = itemAvg.split("\\,");
            boolean matchScouting = false;
            for (Item i : MatchForm.matchItems) {
                if (Integer.parseInt(avgParts[0]) == i.getId()) {
                    visualizedForm += i.getName() + ": ";
                    matchScouting = true;
                } // End if
            } // End for
            if (!matchScouting) continue;
            visualizedForm += avgParts[1] + "\n";
            visualizedForm += "Standard Deviation: " + avgParts[2] + "\n";
            visualizedForm += "Sample Size: " + avgParts[3] + "\n";
            visualizedForm += "\n";
        } // End for

        String[] itemProps = proportions.split("\\|");
        for (String itemProp : itemProps) {
            String[] propParts = itemProp.split("\\,");
            boolean matchScouting = false;
            for (Item i : MatchForm.matchItems) {
                if (Integer.parseInt(propParts[0]) == i.getId()) {
                    visualizedForm += i.getName() + ": ";
                    matchScouting = true;
                } // End if
            } // End for
            if (!matchScouting) continue;
            visualizedForm += propParts[1] + "\n";
            visualizedForm += "Sample Size: " + propParts[2] + "\n";
            visualizedForm += "Success Rate: " + propParts[3] + "\n";
            visualizedForm += "\n";
        } // End for
        return visualizedForm;
    } // End averageFormVisualizer

} // End MatchForm