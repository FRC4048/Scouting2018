import java.util.ArrayList;

/**
 * Created by main on 2/1/18.
 */

public class OverallForm extends Form {
    // [FORM TYPE]|[TABLET_NUMBER]|[SCOUT_NAME]|[RED_LEFT],[RED_CENTER],[RED_RIGHT],[BLUE_LEFT],[BLUE_CENTER],[BLUE_RIGHT]|[FORM_ID]|[MATCH_NUM]|records....
    private ArrayList<Form> teamForms;
    private int[] teamNums;

    public static final class Items {
        public static final Item RED_OWNS_RED_SWITCH = new Item(135, "Red Owns Red Switch", Item.Datatype.INTEGER);
        public static final Item RED_OWNS_SCALE = new Item(136, "Red Owns Scale", Item.Datatype.INTEGER);
        public static final Item RED_OWNS_BLUE_SWITCH = new Item(137, "Red Owns Blue Switch", Item.Datatype.INTEGER);
        public static final Item BLUE_OWNS_BLUE_SWITCH = new Item(138, "Blue Owns Blue Switch", Item.Datatype.INTEGER);
        public static final Item BLUE_OWNS_SCALE = new Item(139, "Blue Owns Scale", Item.Datatype.INTEGER);
        public static final Item BLUE_OWNS_RED_SWITCH = new Item(140, "Blue Owns Red Switch", Item.Datatype.INTEGER);
        public static final Item RED_USED_LEVITATE = new Item(141, "Red Used Levitate", Item.Datatype.INTEGER);
        public static final Item RED_USED_BOOST = new Item(142, "Red Used Boost", Item.Datatype.INTEGER);
        public static final Item RED_USED_FORCE = new Item(143, "Red Used Force", Item.Datatype.INTEGER);
        public static final Item BLUE_USED_LEVITATE = new Item(144, "Blue Used Levitate", Item.Datatype.INTEGER);
        public static final Item BLUE_USED_BOOST = new Item(145, "Blue Used Boost", Item.Datatype.INTEGER);
        public static final Item BLUE_USED_FORCE = new Item(146, "Blue Used Force", Item.Datatype.INTEGER);
        public static final Item YELLOW_CARD = new Item(147, "Yellow Card", Item.Datatype.INTEGER);
        public static final Item RED_CARD = new Item(148, "Red Card", Item.Datatype.INTEGER);
        public static final Item RED_SCORE = new Item(149, "Red Score", Item.Datatype.INTEGER);
        public static final Item BLUE_SCORE = new Item(150, "Blue Score", Item.Datatype.INTEGER);
        public static final Item RED_FOUL_POINTS = new Item(151, "Red Foul Points", Item.Datatype.INTEGER);
        public static final Item BLUE_FOUL_POINTS = new Item(152, "Blue Foul Points", Item.Datatype.INTEGER);
    }

    public static final Item[] overallItems = {
        Items.RED_CARD, Items.RED_FOUL_POINTS, Items.BLUE_FOUL_POINTS, Items.BLUE_OWNS_BLUE_SWITCH, Items.BLUE_OWNS_RED_SWITCH,
            Items.BLUE_OWNS_SCALE, Items.BLUE_SCORE, Items.BLUE_USED_BOOST, Items.BLUE_USED_FORCE, Items.BLUE_USED_LEVITATE, Items.RED_OWNS_BLUE_SWITCH,
            Items.RED_OWNS_RED_SWITCH, Items.RED_OWNS_SCALE, Items.RED_SCORE, Items.RED_USED_BOOST, Items.RED_USED_FORCE, Items.RED_USED_LEVITATE, Items.YELLOW_CARD
    };

    public OverallForm(int tabletNum, int[] teamNums, int matchNum, String scoutName)
    {
        /** Create an encapsulating form that contains the forms for the 5 other teams.
        This form should have all of the records for the overall form. **/
        super(FormType.OVERALL_FORM, teamNums[0], tabletNum, matchNum, scoutName, -1);

        this.teamNums = new int[teamNums.length];
        for (int i = 0; i < this.teamNums.length; i++)
        {
            this.teamNums[i] = teamNums[i];
        }
    }

    public OverallForm(String rawForm)
    {
        super(rawForm);
    }

    public int[] getTeamNums() { return teamNums; }

    public void setTeamNums(int[] teamNums) {
        this.teamNums = new int[teamNums.length];
        for (int i = 0; i < this.teamNums.length; i++)
        {
            this.teamNums[i] = teamNums[i];
        }
    }

    public String toString()
    {
        String returnString = "";
        returnString += getFormType() + Form.ITEM_DELIMITER;
        returnString += getTabletNum() + Form.ITEM_DELIMITER;
        returnString += getScoutName() + Form.ITEM_DELIMITER;
        for (int i = 0; i < teamNums.length - 1; i++)
        {
            returnString += teamNums[i] + Form.ID_DELIMITER;
        }
        returnString += teamNums[teamNums.length-1] + Form.ITEM_DELIMITER;
        returnString += "-1" + Form.ITEM_DELIMITER; // Ignore the alliance
        returnString += getFormID() + Form.ITEM_DELIMITER;
        returnString += getMatchNum();
        ArrayList<Record> records = getAllRecords();
        for (int i = 0; i < records.size(); i++)
        {
            returnString += Form.ITEM_DELIMITER + records.get(i).toString();
        }

        return returnString;
    }
}
