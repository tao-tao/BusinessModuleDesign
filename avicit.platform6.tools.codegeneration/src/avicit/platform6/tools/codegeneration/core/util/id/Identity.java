package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.Serializable;

/**
 * 生成唯一的ID的接口声明
 *
 * @author zhaody@gmail.com
 * @version 1.0
 */
public interface Identity extends Serializable {

    /**
     * 默认的具体的样式，生成32位的唯一的ID。
     * 例子:3914A8AFA4FD464585F3DD023CDAF5CC
     *
     */
    public static final String STYLE_DEFAULT = "STYLE_DEFAULT";

    /**
     * 生成的ID的长度是40位的，唯一ID. <br/>
     * 例子:2007051670E919EC6F3B4534AFDF4FF420A0CD8A
     */
    public static final String STYLE_DATA_DEFAULT = "STYLE_DATA_DEFAULT";

    /**
     * 批量生成的最大长度
     */
    public static final int LEGNTH_MAX=10000;

    /**
     * 批量生成的最小长度
     */
    public static final int LEGNTH_MIN=0;
    /**
     * 获得默认的唯一ID
     * @return String
     */
    public String getIdentity();

    /**
     * 根据样式获得唯一ID
     * @param identity_STYLE String
     * @return String
     */
    public String getIdentity(String identity_STYLE);

    /**
     * 获得默认的唯一ID的数组
     * @param length int
     * @return String[]
     */
    public String[] getIdentitys(int length);

    /**
     * 根据样式获得唯一ID的列表
     * @param identity_STYLE String
     * @param length int
     * @return String[]
     */
    public String[] getIdentitys(String identity_STYLE, int length);

}
