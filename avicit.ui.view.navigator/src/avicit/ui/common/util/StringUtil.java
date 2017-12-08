package avicit.ui.common.util;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;


public final class StringUtil
{

    private StringUtil()
    {
    }

    public static boolean equalsIgnoreCase(String str1, String str2)
    {
        return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
    }

    public static int indexOf(String str, char searchChar)
    {
        if(isEmpty(str))
            return -1;
        else
            return str.indexOf(searchChar);
    }

    public static boolean isWord(String str)
    {
        if(str == null)
            return false;
        char ch[] = str.toCharArray();
        for(int i = 0; i < str.length(); i++)
            if(!Character.isLetterOrDigit(ch[i]) && ch[i] != '_')
                return false;

        return true;
    }

    public static boolean isNumEx(String str)
    {
        if(str == null || str.length() <= 0)
            return false;
        char ch[] = str.toCharArray();
        int i = 0;
        int comcount = 0;
        for(; i < str.length(); i++)
        {
            if(Character.isDigit(ch[i]))
                continue;
            if(ch[i] != '.')
                return false;
            if(i == 0 || i == str.length() - 1)
                return false;
            if(++comcount > 1)
                return false;
        }

        return true;
    }

    public static Object getStringNumber(String str, int index)
    {
        if(str == null)
            return null;
        char ch[] = str.toCharArray();
        String tempStr = "";
        Vector returnNumber = new Vector();
        for(int i = 0; i < str.length(); i++)
        {
            if(Character.isDigit(ch[i]))
            {
                tempStr = (new StringBuilder()).append(tempStr).append(ch[i]).toString();
                continue;
            }
            if(!tempStr.equals(""))
                returnNumber.addElement(tempStr);
            tempStr = "";
        }

        if(!tempStr.equals(""))
            returnNumber.addElement(tempStr);
        if(returnNumber.isEmpty() || index > returnNumber.size())
            return null;
        if(index <= 0)
            return returnNumber;
        else
            return returnNumber.elementAt(index - 1);
    }

    public static String[] sortByLength(String saSource[], boolean bAsc)
    {
        if(saSource == null || saSource.length <= 0)
            return null;
        int iLength = saSource.length;
        String saDest[] = new String[iLength];
        for(int i = 0; i < iLength; i++)
            saDest[i] = saSource[i];

        String sTemp = "";
        int j = 0;
        int k = 0;
        for(j = 0; j < iLength; j++)
            for(k = 0; k < iLength - j - 1; k++)
            {
                if(saDest[k].length() > saDest[k + 1].length() && bAsc)
                {
                    sTemp = saDest[k];
                    saDest[k] = saDest[k + 1];
                    saDest[k + 1] = sTemp;
                    continue;
                }
                if(saDest[k].length() < saDest[k + 1].length() && !bAsc)
                {
                    sTemp = saDest[k];
                    saDest[k] = saDest[k + 1];
                    saDest[k + 1] = sTemp;
                }
            }


        return saDest;
    }

    public static String symbolSBCToDBC(String sSource)
    {
        if(sSource == null || sSource.length() <= 0)
            return sSource;
        int iLen = SBC.length >= DBC.length ? DBC.length : SBC.length;
        for(int i = 0; i < iLen; i++)
            sSource = replace(sSource, SBC[i], DBC[i]);

        return sSource;
    }

    public static String symbolDBCToSBC(String sSource)
    {
        if(sSource == null || sSource.length() <= 0)
            return sSource;
        int iLen = SBC.length >= DBC.length ? DBC.length : SBC.length;
        for(int i = 0; i < iLen; i++)
            sSource = replace(sSource, DBC[i], SBC[i]);

        return sSource;
    }

    public static boolean isEmailAddress(String str)
    {
        if(isEmpty(str))
            return false;
        else
            return emailPattern.matcher(str).matches();
    }

    public static String quoteNullString(String s)
    {
        if(s == null)
            return "Null";
        if(s.trim().length() == 0)
            return "Null";
        else
            return (new StringBuilder()).append("'").append(s.trim()).append("'").toString();
    }

    public static char getCharAtPosDefaultZero(String s, int pos)
    {
        if(s == null)
            return '0';
        if(s.length() <= pos)
            return '0';
        else
            return s.charAt(pos);
    }

    public static String setCharAtPosAppendZero(String s, int pos, char c)
    {
        if(s == null)
            s = "";
        for(; pos > s.length() - 1; s = (new StringBuilder()).append(s).append('0').toString());
        String preString;
        if(pos == 0)
            preString = "";
        else
            preString = s.substring(0, pos);
        String afterString;
        if(pos == s.length() - 1)
            afterString = "";
        else
            afterString = s.substring(pos + 1);
        return (new StringBuilder()).append(preString).append(c).append(afterString).toString();
    }

    public static String fillBlank(String s, int n, boolean isLeft)
    {
        if(n < 0)
            return s;
        if(isEmpty(s))
            return rightPad("", n, " ");
        if(s.length() >= n)
            return s;
        if(isLeft)
            return leftPad(s, n, " ");
        else
            return rightPad(s, n, " ");
    }

    public static int compareVersion(String version1, String version2)
    {
        StringTokenizer st1 = new StringTokenizer(version1, ".");
        StringTokenizer st2 = new StringTokenizer(version2, ".");
        ArrayList al1 = new ArrayList();
        ArrayList al2 = new ArrayList();
        for(; st1.hasMoreTokens(); al1.add(st1.nextToken()));
        for(; st2.hasMoreTokens(); al2.add(st2.nextToken()));
        int size1 = al1.size();
        int size2 = al2.size();
        for(int i = 0; i < size1 && i < size2; i++)
        {
            int v1 = Integer.parseInt((String)al1.get(i));
            int v2 = Integer.parseInt((String)al2.get(i));
            if(v1 > v2)
                return 1;
            if(v1 < v2)
                return -1;
        }

        if(size1 > size2)
            return 1;
        return size1 >= size2 ? 0 : -1;
    }

    public static String deleteAny(String inString, String charsToDelete)
    {
        if(inString == null || charsToDelete == null)
            return inString;
        StringBuffer out = new StringBuffer();
        for(int i = 0; i < inString.length(); i++)
        {
            char c = inString.charAt(i);
            if(charsToDelete.indexOf(c) == -1)
                out.append(c);
        }

        return out.toString();
    }

    public static String quote(String str)
    {
        return str == null ? null : (new StringBuilder()).append("'").append(str).append("'").toString();
    }

    public static Object quoteIfString(Object obj)
    {
        return (obj instanceof String) ? quote((String)obj) : obj;
    }

    public static String unqualify(String qualifiedName)
    {
        return unqualify(qualifiedName, '.');
    }

    public static String unqualify(String qualifiedName, char separator)
    {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static Locale parseLocaleString(String localeString)
    {
        String parts[] = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = parts.length <= 0 ? "" : parts[0];
        String country = parts.length <= 1 ? "" : parts[1];
        String variant = parts.length <= 2 ? "" : parts[2];
        return language.length() <= 0 ? null : new Locale(language, country, variant);
    }

    public static String[] removeDuplicateStrings(String array[])
    {
        if(ArrayUtil.isEmpty(array))
            return array;
        Set set = new TreeSet();
        for(int i = 0; i < array.length; i++)
            set.add(array[i]);

        return ArrayUtil.getStringArrayValues(set);
    }

    public static Properties splitArrayElementsIntoProperties(String array[], String delimiter)
    {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    public static Properties splitArrayElementsIntoProperties(String array[], String delimiter, String charsToDelete)
    {
        if(array == null || array.length == 0)
            return null;
        Properties result = new Properties();
        for(int i = 0; i < array.length; i++)
        {
            String element = array[i];
            if(charsToDelete != null)
                element = deleteAny(array[i], charsToDelete);
            String splittedElement[] = split(element, delimiter);
            if(splittedElement != null)
                result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }

        return result;
    }

    public static String[] tokenizeToStringArray(String str, String delimiters)
    {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens)
    {
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        do
        {
            if(!st.hasMoreTokens())
                break;
            String token = st.nextToken();
            if(trimTokens)
                token = token.trim();
            if(!ignoreEmptyTokens || token.length() > 0)
                tokens.add(token);
        } while(true);
        return ArrayUtil.getStringArrayValues(tokens);
    }

    public static String[] commaDelimitedListToStringArray(String str)
    {
        return split(str, ",");
    }

    public static Set commaDelimitedListToSet(String str)
    {
        Set set = new TreeSet();
        String tokens[] = commaDelimitedListToStringArray(str);
        for(int i = 0; i < tokens.length; i++)
            set.add(tokens[i]);

        return set;
    }

    public static String arrayToDelimitedString(Object arr[], String delim)
    {
        if(arr == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < arr.length; i++)
        {
            if(i > 0)
                sb.append(delim);
            sb.append(arr[i]);
        }

        return sb.toString();
    }

    public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix)
    {
        if(coll == null)
            return "";
        StringBuffer sb = new StringBuffer();
        Iterator it = coll.iterator();
        for(int i = 0; it.hasNext(); i++)
        {
            if(i > 0)
                sb.append(delim);
            sb.append(prefix).append(it.next()).append(suffix);
        }

        return sb.toString();
    }

    public static String collectionToDelimitedString(Collection coll, String delim)
    {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String arrayToCommaDelimitedString(Object arr[])
    {
        return arrayToDelimitedString(arr, ",");
    }

    public static String collectionToCommaDelimitedString(Collection coll)
    {
        return collectionToDelimitedString(coll, ",");
    }

    public static String replace(String src, Map props)
    {
        return replace(src, "${", "}", props);
    }

    public static String replace(String src, String prefix, String suffix, Map props)
    {
        StringBuffer sb;
label0:
        {
            int len1 = prefix.length();
            int len2 = suffix.length();
            sb = new StringBuffer();
            int index1 = src.indexOf(prefix);
            do
            {
                if(index1 < 0)
                    break label0;
                sb.append(src.substring(0, index1));
                src = src.substring(index1 + len1);
                if(src.startsWith(prefix))
                {
                    sb.append(prefix);
                    break label0;
                }
                int index2 = src.indexOf(suffix);
                if(index2 < 0)
                    break;
                String t = src.substring(0, index2);
                Object o = props.get(t);
                String sp = o != null ? o.toString() : "";
                sb.append(sp);
                src = src.substring(index2 + len2);
                index1 = src.indexOf(prefix);
            } while(true);
            sb.append(prefix);
        }
        sb.append(src);
        return new String(sb);
    }

    public static boolean isNotNullAndBlank(String str)
    {
        return !isNullOrBlank(str);
    }

    public static boolean isNullOrBlank(String str)
    {
        return isNull(str) || str.equals("") || str.equals("null");
    }

    public static boolean isNull(String str)
    {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotNull(String str)
    {
        if(str == null || str.trim().length() == 0)
            return false;
        return !str.trim().equalsIgnoreCase("null");
    }

    public static String ifNullToBlank(String str)
    {
        if(isNotNull(str) && !str.trim().equals("null"))
            return str.trim();
        else
            return "";
    }

    public static String ifNullToBlank(Object obj)
    {
        String ret = "";
        String s = String.valueOf(obj);
        if(s == null || "".equals(s) || "null".equals(s) || "NULL".equals(s))
            ret = "";
        else
            ret = s;
        return ret;
    }

    public static boolean hasWildcards(String input)
    {
        return contains(input, "*") || contains(input, "?");
    }

    public static boolean isWildcardMatchOne(String r_Keyword, String r_WildcardMatcher[], boolean r_CaseSensitive)
    {
        if(null == r_WildcardMatcher)
            return true;
        for(int i = 0; i < r_WildcardMatcher.length; i++)
        {
            String t_WildCardMatcher = r_WildcardMatcher[i];
            if(isWildcardMatch(r_Keyword, t_WildCardMatcher, r_CaseSensitive))
                return true;
        }

        return false;
    }

    public static boolean isWildcardMatchAll(String r_Keyword, String r_WildcardMatcher[], boolean r_CaseSensitive)
    {
        if(null == r_WildcardMatcher)
            return true;
        for(int i = 0; i < r_WildcardMatcher.length; i++)
        {
            String t_WildCardMatcher = r_WildcardMatcher[i];
            if(!isWildcardMatch(r_Keyword, t_WildCardMatcher, r_CaseSensitive))
                return false;
        }

        return true;
    }

    public static boolean isWildcardMatch(String r_Keyword, String r_WildcardMatcher)
    {
        return isWildcardMatch(r_Keyword, r_WildcardMatcher, true);
    }

    public static boolean isWildcardMatch(String r_Keyword, String r_WildcardMatcher, boolean r_CaseSensitive)
    {
        if(r_Keyword == null && r_WildcardMatcher == null)
            return true;
        if(r_Keyword == null || r_WildcardMatcher == null)
            return false;
        if(!r_CaseSensitive)
        {
            r_Keyword = r_Keyword.toLowerCase();
            r_WildcardMatcher = r_WildcardMatcher.toLowerCase();
        }
        String t_SplitValues[] = splitOnTokens(r_WildcardMatcher);
        boolean t_Chars = false;
        int t_Index = 0;
        int t_WildIndex = 0;
        Stack t_BackStack = new Stack();
        do
        {
            if(t_BackStack.size() > 0)
            {
                int array[] = (int[])(int[])t_BackStack.pop();
                t_WildIndex = array[0];
                t_Index = array[1];
                t_Chars = true;
            }
            for(; t_WildIndex < t_SplitValues.length; t_WildIndex++)
            {
                if(t_SplitValues[t_WildIndex].equals("?"))
                {
                    t_Index++;
                    t_Chars = false;
                    continue;
                }
                if(t_SplitValues[t_WildIndex].equals("*"))
                {
                    t_Chars = true;
                    if(t_WildIndex == t_SplitValues.length - 1)
                        t_Index = r_Keyword.length();
                    continue;
                }
                if(t_Chars)
                {
                    t_Index = r_Keyword.indexOf(t_SplitValues[t_WildIndex], t_Index);
                    if(t_Index == -1)
                        break;
                    int repeat = r_Keyword.indexOf(t_SplitValues[t_WildIndex], t_Index + 1);
                    if(repeat >= 0)
                        t_BackStack.push(new int[] {
                            t_WildIndex, repeat
                        });
                } else
                if(!r_Keyword.startsWith(t_SplitValues[t_WildIndex], t_Index))
                    break;
                t_Index += t_SplitValues[t_WildIndex].length();
                t_Chars = false;
            }

            if(t_WildIndex == t_SplitValues.length && t_Index == r_Keyword.length())
                return true;
        } while(t_BackStack.size() > 0);
        return false;
    }

    private static String[] splitOnTokens(String r_Text)
    {
        if(r_Text.indexOf("?") == -1 && r_Text.indexOf("*") == -1)
            return (new String[] {
                r_Text
            });
        char t_Array[] = r_Text.toCharArray();
        ArrayList t_List = new ArrayList();
        StringBuffer t_Buffer = new StringBuffer();
        for(int i = 0; i < t_Array.length; i++)
            if(t_Array[i] == '?' || t_Array[i] == '*')
            {
                if(t_Buffer.length() != 0)
                {
                    t_List.add(t_Buffer.toString());
                    t_Buffer.setLength(0);
                }
                if(t_Array[i] == '?')
                {
                    t_List.add("?");
                    continue;
                }
                if(t_List.size() == 0 || i > 0 && !t_List.get(t_List.size() - 1).equals("*"))
                    t_List.add("*");
            } else
            {
                t_Buffer.append(t_Array[i]);
            }

        if(t_Buffer.length() != 0)
            t_List.add(t_Buffer.toString());
        return (String[])(String[])t_List.toArray(new String[0]);
    }

    public static boolean isIn(String r_Source, String r_Target[], boolean r_CaseSensitive)
    {
        for(int i = 0; i < r_Target.length; i++)
        {
            String t_Value = r_Target[i];
            if(r_CaseSensitive)
            {
                if(equals(r_Source, t_Value))
                    return true;
                continue;
            }
            if(equalsIgnoreCase(r_Source, t_Value))
                return true;
        }

        return false;
    }

    public static boolean isIn(String r_Source, Collection r_Target)
    {
        for(Iterator t_Iterator = r_Target.iterator(); t_Iterator.hasNext();)
        {
            String t_Value = (String)t_Iterator.next();
            if(equalsIgnoreCase(r_Source, t_Value))
                return true;
        }

        return false;
    }

    public static String targetEndStyle(String name)
    {
        return (new StringBuilder()).append("</").append(name).append(">").toString();
    }

    public static String valueToSetStyle(String value)
    {
        if(value == null)
            value = "";
        return (new StringBuilder()).append("=\"").append(value).append("\"").toString();
    }

    public static boolean equal(String s1, String s2)
    {
        if(s1 == s2)
            return true;
        if(s1 == null)
            s1 = "";
        if(s2 == null)
            s2 = "";
        s1 = s1.trim();
        s2 = s2.trim();
        return s1.equals(s2);
    }

    public static String concat(Object args[])
    {
        StringBuffer buf = new StringBuffer();
        Object arr$[] = args;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            Object arg = arr$[i$];
            buf.append(arg);
        }

        return buf.toString();
    }

    public static String format(String s, Object params[])
    {
        String message = s;
        if(message == null)
            return "";
        if(params != null && params.length > 0)
            message = (new MessageFormat(message)).format(((Object) (params)));
        return message;
    }

    public static boolean startsWithIgnoreCase(String str, String prefix)
    {
        if(str == null || prefix == null)
            return false;
        if(str.startsWith(prefix))
            return true;
        if(str.length() < prefix.length())
        {
            return false;
        } else
        {
            String lcStr = str.substring(0, prefix.length()).toLowerCase();
            String lcPrefix = prefix.toLowerCase();
            return lcStr.equals(lcPrefix);
        }
    }

    public static boolean isNotEmpty(String str)
    {
        return str != null && str.length() > 0;
    }

    public static String substringAfter(String str, String separator)
    {
        if(isEmpty(str))
            return str;
        if(separator == null)
            return "";
        int pos = str.indexOf(separator);
        if(pos == -1)
            return "";
        else
            return str.substring(pos + separator.length());
    }

    public static String substringBefore(String str, String separator)
    {
        if(isEmpty(str) || separator == null)
            return str;
        if(separator.length() == 0)
            return "";
        int pos = str.indexOf(separator);
        if(pos == -1)
            return str;
        else
            return str.substring(0, pos);
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(String str)
    {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return true;
        for(int i = 0; i < strLen; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    public static String[] split(String str, char separatorChar)
    {
        return splitWorker(str, separatorChar, false);
    }

    public static String[] split(String str, String separatorChars)
    {
        return splitWorker(str, separatorChars, -1, false);
    }

    public static String[] split(String str, String separatorChars, int max)
    {
        return splitWorker(str, separatorChars, max, false);
    }

    public static boolean contains(String str, String searchStr)
    {
        if(str == null || searchStr == null)
            return false;
        else
            return str.indexOf(searchStr) >= 0;
    }

    public static String deleteWhitespace(String str)
    {
        if(isEmpty(str))
            return str;
        int sz = str.length();
        char chs[] = new char[sz];
        int count = 0;
        for(int i = 0; i < sz; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                chs[count++] = str.charAt(i);

        if(count == sz)
            return str;
        else
            return new String(chs, 0, count);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens)
    {
        if(str == null)
            return null;
        int len = str.length();
        if(len == 0)
            return EMPTY_STRING_ARRAY;
        List list = new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while(i < len) 
            if(str.charAt(i) == separatorChar)
            {
                if(match || preserveAllTokens)
                {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
            } else
            {
                lastMatch = false;
                match = true;
                i++;
            }
        if(match || preserveAllTokens && lastMatch)
            list.add(str.substring(start, i));
        return (String[])(String[])list.toArray(new String[list.size()]);
    }

    public static String replace(String text, String repl, String with)
    {
        return replace(text, repl, with, -1);
    }

    public static String replace(String text, String repl, String with, int max)
    {
        if(text == null || isEmpty(repl) || with == null || max == 0)
            return text;
        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        do
        {
            if((end = text.indexOf(repl, start)) == -1)
                break;
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
        } while(--max != 0);
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String leftPad(String str, int size)
    {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar)
    {
        if(str == null)
            return null;
        int pads = size - str.length();
        if(pads <= 0)
            return str;
        if(pads > 8192)
            return leftPad(str, size, String.valueOf(padChar));
        else
            return padding(pads, padChar).concat(str);
    }

    private static String padding(int repeat, char padChar)
    {
        String pad = PADDING[padChar];
        if(pad == null)
            pad = String.valueOf(padChar);
        for(; pad.length() < repeat; pad = pad.concat(pad));
        PADDING[padChar] = pad;
        return pad.substring(0, repeat);
    }

    public static String leftPad(String str, int size, String padStr)
    {
        if(str == null)
            return null;
        if(isEmpty(padStr))
            padStr = " ";
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if(pads <= 0)
            return str;
        if(padLen == 1 && pads <= 8192)
            return leftPad(str, size, padStr.charAt(0));
        if(pads == padLen)
            return padStr.concat(str);
        if(pads < padLen)
            return padStr.substring(0, pads).concat(str);
        char padding[] = new char[pads];
        char padChars[] = padStr.toCharArray();
        for(int i = 0; i < pads; i++)
            padding[i] = padChars[i % padLen];

        return (new String(padding)).concat(str);
    }

    public static String rightPad(String str, int size)
    {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar)
    {
        if(str == null)
            return null;
        int pads = size - str.length();
        if(pads <= 0)
            return str;
        if(pads > 8192)
            return rightPad(str, size, String.valueOf(padChar));
        else
            return str.concat(padding(pads, padChar));
    }

    public static String rightPad(String str, int size, String padStr)
    {
        if(str == null)
            return null;
        if(isEmpty(padStr))
            padStr = " ";
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if(pads <= 0)
            return str;
        if(padLen == 1 && pads <= 8192)
            return rightPad(str, size, padStr.charAt(0));
        if(pads == padLen)
            return str.concat(padStr);
        if(pads < padLen)
            return str.concat(padStr.substring(0, pads));
        char padding[] = new char[pads];
        char padChars[] = padStr.toCharArray();
        for(int i = 0; i < pads; i++)
            padding[i] = padChars[i % padLen];

        return str.concat(new String(padding));
    }

    public static boolean equals(String str1, String str2)
    {
        return str1 != null ? str1.equals(str2) : str2 == null;
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens)
    {
        if(str == null)
            return null;
        int len = str.length();
        if(len == 0)
            return EMPTY_STRING_ARRAY;
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if(separatorChars == null)
            while(i < len) 
                if(Character.isWhitespace(str.charAt(i)))
                {
                    if(match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if(sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
        else
        if(separatorChars.length() == 1)
        {
            char sep = separatorChars.charAt(0);
            while(i < len) 
                if(str.charAt(i) == sep)
                {
                    if(match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if(sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
        } else
        {
            while(i < len) 
                if(separatorChars.indexOf(str.charAt(i)) >= 0)
                {
                    if(match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if(sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
        }
        if(match || preserveAllTokens && lastMatch)
            list.add(str.substring(start, i));
        return (String[])(String[])list.toArray(new String[list.size()]);
    }

    public static String removeStart(String str, String remove)
    {
        if(isEmpty(str) || isEmpty(remove))
            return str;
        if(str.startsWith(remove))
            return str.substring(remove.length());
        else
            return str;
    }

    public static String removeEnd(String str, String remove)
    {
        if(isEmpty(str) || isEmpty(remove))
            return str;
        if(str.endsWith(remove))
            return str.substring(0, str.length() - remove.length());
        else
            return str;
    }

    public static String remove(String str, String remove)
    {
        if(isEmpty(str) || isEmpty(remove))
            return str;
        else
            return replace(str, remove, "", -1);
    }

    public static String remove(String str, char remove)
    {
        if(isEmpty(str) || str.indexOf(remove) == -1)
            return str;
        char chars[] = str.toCharArray();
        int pos = 0;
        for(int i = 0; i < chars.length; i++)
            if(chars[i] != remove)
                chars[pos++] = chars[i];

        return new String(chars, 0, pos);
    }

    private static final String EmailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$";
    private static final transient Pattern emailPattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})$");
    private static final String DEFAULT_PREFIX = "${";
    private static final String DEFAULT_SUFFIX = "}";
    static final String SBC[] = {
        "\uFF0C", "\u3002", "\uFF1B", "\u201C", "\u201D", "\uFF1F", "\uFF01", "\uFF08", "\uFF09", "\uFF1A", 
        "\u2014\u2014", "\u3001"
    };
    static final String DBC[] = {
        ",", ".", ";", "\"", "\"", "?", "!", "(", ")", ":", 
        "_", ","
    };
    private static final int PAD_LIMIT = 8192;
    private static final String PADDING[] = new String[65535];
    private static final String EMPTY_STRING_ARRAY[] = new String[0];

}