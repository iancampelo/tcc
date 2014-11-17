package assistente.br.ucb.tcc.assistentereflexivo;

import android.content.Context;

/**
 * Created by ian.campelo on 11/16/14.
 */
public enum MyEnum {
    FIRST(1,R.string.error),
    SECOND(2,R.string.strategy);

    private int mId;
    private int mDescriptionResourceId;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private MyEnum(int id,int descriptionResourceId) {
        mId = id;
        mDescriptionResourceId = descriptionResourceId;
    }

    @Override
    public String toString() {
        return getmContext().getString(mDescriptionResourceId);
    }
}
