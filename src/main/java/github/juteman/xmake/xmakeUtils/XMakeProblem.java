package github.juteman.xmake.xmakeUtils;

public class XMakeProblem {
    private String file;
    private String line;
    private String column;
    private String kind;
    private String message;

    public XMakeProblem(String file, String line, String column, String kind, String message)
    {
        this.file = file;
        this.line = line;
        this.column = column;
        this.kind = kind;
        this.message = message;
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
