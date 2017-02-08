package buaa.buaahelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContentProvider {

    /**
     * An array of sample (dummy) items.
     */
    private   List<CommonItemForList> ITEMS = new ArrayList<CommonItemForList>();
    protected String Username,Password;
    /**
     * A map of sample (dummy) items, by ID.
     */
    ///private   Map<Integer, CommonItemForList> ITEM_MAP = new HashMap<Integer, CommonItemForList>();

    private String getUsername() {
        return Username;
    }

    private void setUsername(String username) {
        Username = username;
    }

    private String getPassword() {
        return Password;
    }

    private void setPassword(String password) {
        Password = password;
    }

    public int getDataSize()
    {
        return ITEMS.size();
    }
    ContentProvider() {
        // Add some sample items.
        for (int i = 1; i <= 25; i++) {
            addItem(createCommonItemForList(i));
        }
    }
    ContentProvider(Object o)  // create for doing nothing, just for performance of subclass creating
    {

    }
    public List<CommonItemForList> getDataList()
    {
        return ITEMS;
    }
    protected  void clear()
    {
        ITEMS.clear();
       // ITEM_MAP.clear();
    }
    protected void addAll(List<CommonItemForList> list)
    {
        ITEMS.addAll(list);
    }
    public  void addItem(CommonItemForList item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
    }

    private static CommonItemForList createCommonItemForList(int position) {
        return new CommonItemForList(0,"examplexxxx "+position+" ", "http://" , new Date());
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }



}
