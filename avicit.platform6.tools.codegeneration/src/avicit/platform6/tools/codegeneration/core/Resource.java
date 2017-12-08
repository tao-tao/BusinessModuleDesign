/********************************************************************
 * Copyright (c) 2004 Ultimania Organization. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 * Contributors:
 *     tidus - initial implementation. 
 *******************************************************************/

package avicit.platform6.tools.codegeneration.core;

/**
 * Message Resource.
 * ResourceMessages.properties and ResourceMessages_ja.properties are
 * generated from this class.
 * @msg.bundle 
 */
public class Resource {
    
    /** 
     * @msg.bundle msg="Browse"
     * @msg.bundle msg="�Q��" language="ja"
     */
    public static final String JDBC_BROWSE="jdbc_browse";

    /** 
     * @msg.bundle msg="JDBC jar"
     * @msg.bundle msg="JDBC jar" language="ja"
     */
    public static final String JDBC_JAR="jdbc_jar";

    /** 
     * @msg.bundle msg="JDBC Driver:"
     * @msg.bundle msg="JDBC�h���C�o:" language="ja"
     */
    public static final String DATABASE_DRIVER="database_driver";

    /** 
     * @msg.bundle msg="Database URI:"
     * @msg.bundle msg="�f�[�^�x�[�XURI:" language="ja"
     */
    public static final String DATABASE_URI="database_uri";

    /** 
     * @msg.bundle msg="User:"
     * @msg.bundle msg="���[�U:" language="ja"
     */
    public static final String DATABASE_USER="database_user";

    /** 
     * @msg.bundle msg="Password:"
     * @msg.bundle msg="�p�X���[�h:" language="ja"
     */
    public static final String DATABASE_PASSWORD="database_password";

    /** 
     * @msg.bundle msg="Schema:"
     * @msg.bundle msg="�X�L�[�}:" language="ja"
     */
    public static final String DATABASE_SCHEMA="database_schema";

    /** 
     * @msg.bundle msg="Category:"
     * @msg.bundle msg="�J�e�S��:" language="ja"
     */
    public static final String DATABASE_CATALOG="database_category";

    /** 
     * @msg.bundle msg="Load Table:"
     * @msg.bundle msg="�e�[�u���̓ǂݍ���:" language="ja"
     */
    public static final String LOADTABLE="loadtable";

    /** 
     * @msg.bundle msg="Connection fail. Check JDBC configuration."
     * @msg.bundle msg="�R�l�N�V�����̎擾�Ɏ��s���܂����BJDBC�̐ݒ��m�F���Ă��������B" language="ja"
     */
    public static final String ERROR_CONNECTION="error_connection";

    /** 
     * @msg.bundle msg="Package:"
     * @msg.bundle msg="�p�b�P�[�W:" language="ja"
     */
    public static final String PACKAGE="package";

    /** 
     * @msg.bundle msg="Directory:"
     * @msg.bundle msg="�f�B���N�g��:" language="ja"
     */
    public static final String DIRECTORY="directory";

    /** 
     * @msg.bundle msg="Tables:"
     * @msg.bundle msg="�e�[�u��:" language="ja"
     */
    public static final String TABLES="tables";
    
    public static final String TABLESSUB="tablesub";
    
    public static final String RELATIONMAIN="relationmain";
    
    public static final String RELATIONSUB="relationsub";
    
    public static final String MAVEN="maven";

    /** 
     * @msg.bundle msg="Plugin"
     * @msg.bundle msg="�v���O�C��" language="ja"
     */
    public static final String PLUGIN="plugin";
    
    public static final String MAINSUBKEY ="mainsubkey";
    
    public static final String SINGLEFLOW ="singleflow";

    /** 
     * @msg.bundle msg="Option"
     * @msg.bundle msg="�ڍ�" language="ja"
     */
    public static final String OPTION="option";

    /** 
     * @msg.bundle msg="Run"
     * @msg.bundle msg="�s" language="ja"
     */
    public static final String RUN="run";

    /** 
     * @msg.bundle msg="View"
     * @msg.bundle msg="�r���[" language="ja"
     */
	public static final String DATABASE_VIEW = "view";

    /** 
     * @msg.bundle msg="ClassNotFoundException was occured. Please add required jar files by jdbc driver into eclipse project."
     * @msg.bundle msg="ClassNotFoundException���������܂����BJDBC�h���C�o���v������jar�t�@�C����Eclipse�v���W�F�N�g�ɒǉB��ĉ������B" language="ja"
     */
	public static final String INVALID_CLASSPATH = "invalid_classpath";


    /** 
     * @msg.bundle msg="table is not selected"
     * @msg.bundle msg="�e�[�u�����I�Ⳃ�Ă��܂���" language="ja"
     */
	public static final String TABLE_NOT_SELECTED = "table_not_selected";

    /** 
     * @msg.bundle msg="Please specify src path in java build property."
     * @msg.bundle msg="Java�̃r���h�v���p�e�B�Ń\�[�X�p�X��w�肵�ĉ������B" language="ja"
     */
	public static final String ERROR_SRC_PATH = "error.src.path";
	
	
	public static final String jspMainChild = "jspMainChild";
	public static final String jspSingleWorkFlow = "jspSingleWorkFlow";
	public static final String singleTable = "singleTable";
	public static final String singleTabletree = "singleTabletree";
}
