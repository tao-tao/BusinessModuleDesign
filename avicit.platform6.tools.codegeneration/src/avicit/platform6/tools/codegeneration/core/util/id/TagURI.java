package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.PrintStream;
import java.util.Calendar;

class TagURI {
    
    private final String mDesc;
    
    public TagURI(String authority, String identifier, Calendar date) {
        StringBuffer b = new StringBuffer();
        b.append("tag:");
        b.append(authority);
        if(date != null) {
            b.append(',');
            b.append(date.get(1));
            int month = (date.get(2) - 0) + 1;
            int day = date.get(5);
            if(month != 1 || day != 1) {
                b.append('-');
                b.append(month);
            }
            if(day != 1) {
                b.append('-');
                b.append(day);
            }
        }
        b.append(':');
        b.append(identifier);
        mDesc = b.toString();
    }
    
    public String toString() {
        return mDesc;
    }
    
    public boolean equals(Object o) {
        if(o instanceof TagURI) {
            return mDesc.equals(((TagURI)o).toString());
        } else {
            return false;
        }
    }
    
    public static void main(String args[]) {
        String auths[] = {
            "www.w3c.org", "www.google.com", "www.fi", "tatu.saloranta@iki.fi"
        };
        String ids[] = {
            "1234", "/home/billg/public_html/index.html", "6ba7b810-9dad-11d1-80b4-00c04fd430c8", "foobar"
        };
        Calendar c = null;
        String auth = null;
        for(int i = 0; i < 4; i++) {
            System.out.println("\n");
            switch(i) {
                case 2: // '\002'
                    c.add(2, 1);
                    break;
                    
                case 3: // '\003'
                    c.add(5, -7);
                    break;
            }
            for(int j = 0; j < 4; j++) {
                TagURI t = new TagURI(auths[i], ids[j], c);
                System.out.println("tagURI: " + t);
            }
            
            if(c == null) {
                c = Calendar.getInstance();
            }
        }
        
    }
}
