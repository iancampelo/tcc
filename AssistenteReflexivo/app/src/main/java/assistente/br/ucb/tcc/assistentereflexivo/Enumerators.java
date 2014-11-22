package assistente.br.ucb.tcc.assistentereflexivo;

import android.content.Context;

/**
 * Created by ian.campelo on 11/16/14.
 */
public class Enumerators{

    private static Context mContext;


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context ctx) {
        mContext = ctx;
    }

    enum EnumSpinPrediction {
        INCORRECT(-1, R.string.incorrect),
        P_CORRECT(0, R.string.partially_correct),
        CORRECT(1, R.string.correct);

        private int idString, id;


        EnumSpinPrediction(int _id, int _idString) {
            setIdString(_idString);
            setId(_id);
        }

        public String resource() {

            return mContext.getString(idString);
        }

        @Override
        public String toString() {
            return resource();
        }

        public int getIdString() {
            return idString;
        }

        public int getId() {
            return id;
        }

        public void setIdString(int _idString) {
            idString = _idString;
        }

        public void setId(int _id) {
            id = _id;
        }

        public static String getById(long id) {
            for (EnumSpinPrediction e : EnumSpinPrediction.values()) {
                if (id == e.getId()) return e.toString();
            }
            throw new IllegalArgumentException("oh no");
        }

        public static int findIDbyString(String abbr) {
            for (EnumSpinPrediction v : values()) {
                if (v.resource().equals(abbr)) {
                    return v.getId();
                }
            }
            return -192930;
        }
    }

}