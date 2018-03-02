package shreya.overallscoutingform2018;
import java.util.ArrayList;
import java.util.Collection;

public class Form {
	
	private String rawForm;

	private FormType formType;
	private int tabletNum;
	private int teamNum;
	private int matchNum;
	private String scoutName;
	private int alliance;
	private int formID;
	// database report id
	private int reportID;
	ArrayList<Record> records;
	
	public static final String FORM_DELIMITER = "||";
	public static final String ITEM_DELIMITER = "|";
	public static final String ID_DELIMITER = ",";

	public static final int RED_ALLIANCE_NUMBER = 0;
	public static final int BLUE_ALLIANCE_NUMBER = 1;
	
	public enum FormType {
		PRESCOUTING_FORM, MATCH_FORM, OVERALL_FORM
	}
	
	public static final class FormOrder {
		public static final int FORM_TYPE = 0;
		public static final int TABLET_NUM = 1;
		public static final int SCOUT_NAME = 2;
		public static final int TEAM_NUM = 3;
		public static final int ALLIANCE = 4;
		public static final int MATCH_NUM = 5;
		
		public static final int highestIndex() {
			return MATCH_NUM;
		}
	}

	// For the scenario of trying to save a form to the database. Do not know the formID yet.
	public Form(FormType formType, int tabletNum, int teamNum, String scoutName, int alliance) {
		this.formType = formType;
		this.tabletNum = tabletNum;
		this.teamNum = teamNum;
		this.matchNum = -1;
		this.scoutName = scoutName;
		this.reportID = -1;
		this.alliance = alliance;
		records = new ArrayList<>();
		this.rawForm = null;
	}

    // For the scenario of trying to save a form to the database. Do not know the formID yet.
	public Form(FormType formType, int tabletNum, int teamNum, int matchNum, String scoutName, int alliance) {
		this.formType = formType;
		this.tabletNum = tabletNum;
		this.teamNum = teamNum;
		this.matchNum = matchNum;
		this.scoutName = scoutName;
		this.reportID = -1;
		this.alliance = alliance;
		records = new ArrayList<>();
		this.rawForm = null;
	}

	// For the scenario of fetching from the database. Do know the formID.
	public Form(int reportID, FormType formType, int tabletNum, int teamNum, String scoutName, int alliance, int formID) {
		this.formType = formType;
		this.tabletNum = tabletNum;
		this.teamNum = teamNum;
		this.matchNum = -1;
		this.scoutName = scoutName;
		this.reportID = reportID;
		this.alliance = alliance;
		this.formID = formID;
		records = new ArrayList<>();
		this.rawForm = null;
	}

    // For the scenario of fetching from the database. Do know the formID.
	public Form(int reportID, FormType formType, int tabletNum, int teamNum, int matchNum, String scoutName, int alliance, int formID) {
		this.formType = formType;
		this.tabletNum = tabletNum;
		this.teamNum = teamNum;
		this.matchNum = matchNum;
		this.scoutName = scoutName;
		this.reportID = reportID;
		this.alliance = alliance;
		this.formID = formID;
		records = new ArrayList<>();
		this.rawForm = null;
	}

	// For the scenario of trying to transfer a form to the Java processing app.
	public Form(String rawForm) {
		this.rawForm = rawForm;
		records = new ArrayList<>();
		breakDownForm(this);
	}

	public String getRawForm() {
		return rawForm;
	}

	public void setRawForm(String rawForm) {
		this.rawForm = rawForm;
		breakDownForm(this);
	}

	public FormType getFormType() {
		return formType;
	}

	public int getTabletNum() {
		return tabletNum;
	}
	
	public int getTeamNum() {
		return teamNum;
	}

	public int getReportID() {
		return reportID;
	}
	
	public int getMatchNum() {
		return matchNum;
	}

	public int getAlliance() { return alliance; }

	public int getFormID() { return formID; }

	public void setAlliance(int alliance) { this.alliance = alliance; }

	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}

	public void setTabletNum(int tabletNum) {
		this.tabletNum = tabletNum;
	}

	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}

	public void setReportID(int reportID) {
		this.reportID = reportID;
	}

	public void setFormID(int formID) { this.formID = formID; }
	
	public String getScoutName() {
		return scoutName;
	}

	public void setScoutName(String scoutName) {
		this.scoutName = scoutName;
	}

	public ArrayList<Record> getAllRecords() {
		return records;
	}
	
	public Record getRecord(int index) {
		return records.get(index);
	}
	
	public boolean addRecord(Record record) {
		return records.add(record);
	}
	
	public boolean addRecords(Collection<? extends Record> records) {
		return this.records.addAll(records);
	}
	
	public boolean addRecords(Record[] records) {
		boolean bool = true;
		for (int i = 0; i < records.length; i++) 
			if (this.records.add(records[i]) == false) bool = false;
		return bool;
	}
	
	public Record[] addRecords(String rawRecords) {
		rawForm += "|" + rawRecords;
		this.addRecords(breakDownRecords(rawRecords, this.formType));
		return null;
	}
	
	public boolean removeRecord(Record record) {
		return records.remove(record);
	}
	
	public Record removeRecord(int index) {
		return records.remove(index);
	}
	
	public boolean removeRecords(Collection<? extends Record> records) {
		return this.records.removeAll(records);
	}
	
	public boolean removeRecords(Record[] records) {
		boolean bool = true;
		for (int i = 0; i < records.length; i++) 
			if (this.records.remove(records[i]) == false) bool = false;
		return bool;
	}
	
	protected static void breakDownForm(Form form) {
		String rawForm = form.getRawForm();
		if (rawForm != null) {
			String[] items = rawForm.split("\\" + ITEM_DELIMITER);
			int type = Integer.parseInt(items[FormOrder.FORM_TYPE]);
			if (type == FormType.MATCH_FORM.ordinal())
			{
				form = new MatchForm(-1, Integer.parseInt(items[FormOrder.TABLET_NUM]), Integer.parseInt(items[FormOrder.TEAM_NUM]), Integer.parseInt(items[FormOrder.MATCH_NUM]), items[FormOrder.SCOUT_NAME], Integer.parseInt(items[FormOrder.ALLIANCE]), -1);
				// TO-DO: Dan deal with this later
//				form = new MatchForm(-1, Integer.parseInt(items[FormOrder.TABLET_NUM]), Integer.parseInt(items[FormOrder.TEAM_NUM]),
//						Integer.parseInt(items[FormOrder.MATCH_NUM]), items[FormOrder.SCOUT_NAME],
//                        Integer.parseInt(items[FormOrder.ALLIANCE]), Integer.parseInt(items[FormOrder.FORM_ID]));
			}
			else if (type == FormType.PRESCOUTING_FORM.ordinal()) form.setFormType(FormType.PRESCOUTING_FORM);
			else if (type == FormType.OVERALL_FORM.ordinal())
			{
				String teamNumsString = items[FormOrder.TEAM_NUM];
				String[] teamNumsStrings = teamNumsString.split("\\" + ID_DELIMITER);
				int[] teamNums = new int[teamNumsStrings.length];
				for (int i = 0; i < teamNums.length; i++)
				{
					teamNums[i] = Integer.parseInt(teamNumsStrings[i]);
				}

				form = new OverallForm(Integer.parseInt(items[FormOrder.TABLET_NUM]), teamNums, Integer.parseInt(items[FormOrder.MATCH_NUM]), items[FormOrder.SCOUT_NAME]);
			}
			String rawRecords = "";
			for (int i = 0; i < items.length-(FormOrder.highestIndex()+1); i++) {
				if (i == 0) rawRecords += items[FormOrder.highestIndex()+i+1];
				else rawRecords += ITEM_DELIMITER + items[FormOrder.highestIndex()+i+1];
			}
			if (!rawRecords.isEmpty()) form.addRecords(breakDownRecords(rawRecords, form.getFormType()));
		}
	}
	
	protected static Record[] breakDownRecords(String rawRecords, FormType type) {
		ArrayList<Record> formRecords = new ArrayList<>();
		String[] records = rawRecords.split("\\" + ITEM_DELIMITER);
		for (String record : records) {
			String[] elements = record.split("\\" + ID_DELIMITER);
			formRecords.add(new Record(elements[1], Integer.parseInt(elements[0])));
		}
		return formRecords.toArray(new Record[0]);
	}
	
	@Override
	public String toString() {
		if (rawForm != null) return rawForm;
		else {
			rawForm = formType + ITEM_DELIMITER;
			rawForm += tabletNum + ITEM_DELIMITER;
			rawForm += scoutName + ITEM_DELIMITER;
			rawForm += teamNum + ITEM_DELIMITER;
			rawForm += alliance + ITEM_DELIMITER;
			rawForm += formID + ITEM_DELIMITER;
			rawForm += matchNum;
			for (int i = 0; i < records.size(); i++)
				rawForm += ITEM_DELIMITER + records.get(i).toString();
			return rawForm;
		}
	}

}