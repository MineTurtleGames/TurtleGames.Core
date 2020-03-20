package co.turtlegames.core.achievement;

public enum AchievementFlagType {

    HIDDEN_DESC(null),
    UNIQUE_REWARD((String inp) -> {
        try {
            return Class.forName(inp).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    });

    public interface IConverter {
        public Object convert(String in);
    }

    private IConverter _converter;

    AchievementFlagType(IConverter converter) {
        _converter = converter;
    }

    public Object convertData(String data) {

        if(_converter == null || data == null)
            return data;

        return _converter.convert(data);

    }

}
