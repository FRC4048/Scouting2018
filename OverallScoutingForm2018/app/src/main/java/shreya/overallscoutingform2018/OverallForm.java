package shreya.overallscoutingform2018;

import java.util.ArrayList;

/**
 * Created by main on 2/1/18.
 */

public class OverallForm extends Form {
    ArrayList<Form> teamForms;
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
        super(FormType.OVERALL_FORM, teamNums[0], tabletNum, matchNum, scoutName); // Create an encapsulating form that contains the forms for the 5 other teams.
        // This form should have all of the records for the overall form.
        for (int i = 1; i < teamNums.length; i++)
        {
            Form teamForm = new Form(FormType.OVERALL_FORM, tabletNum, teamNums[i], matchNum, scoutName);
            teamForms.add(teamForm);
        }
    }

    public OverallForm(String rawForm)
    {
        super(rawForm);
    }

    /** Overridden from parent class. **/
    private static void breakDownForm(Form form) {

    }

    /** Overridden from parent class. **/
    private static Record[] breakDownRecords(String rawRecords, FormType type)
    {

    }
}