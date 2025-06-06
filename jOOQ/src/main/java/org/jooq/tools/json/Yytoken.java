package org.jooq.tools.json;

/**
 * @deprecated - 3.21.0 - [#18329] - This shaded third party dependency will be
 *             removed without replacement. Please use any other JSON parser,
 *             instead - e.g. Jackson.
 */
@Deprecated(forRemoval = true)
public class Yytoken {
    public static final int TYPE_VALUE=0;//JSON primitive value: string,number,boolean,null
    public static final int TYPE_LEFT_BRACE=1;
    public static final int TYPE_RIGHT_BRACE=2;
    public static final int TYPE_LEFT_SQUARE=3;
    public static final int TYPE_RIGHT_SQUARE=4;
    public static final int TYPE_COMMA=5;
    public static final int TYPE_COLON=6;
    public static final int TYPE_EOF=-1;//end of file

    public int type=0;
    public Object value=null;

    public Yytoken(int type,Object value){
        this.type=type;
        this.value=value;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        switch(type){
            case TYPE_VALUE:
                sb.append("VALUE(").append(value).append(")");
                break;
            case TYPE_LEFT_BRACE:
                sb.append("LEFT BRACE({)");
                break;
            case TYPE_RIGHT_BRACE:
                sb.append("RIGHT BRACE(})");
                break;
            case TYPE_LEFT_SQUARE:
                sb.append("LEFT SQUARE([)");
                break;
            case TYPE_RIGHT_SQUARE:
                sb.append("RIGHT SQUARE(])");
                break;
            case TYPE_COMMA:
                sb.append("COMMA(,)");
                break;
            case TYPE_COLON:
                sb.append("COLON(:)");
                break;
            case TYPE_EOF:
                sb.append("END OF FILE");
                break;
        }
        return sb.toString();
    }
}