package it.nerr.wolframalpha4j.types;

import java.util.List;
public record QueryResult(
        String query,
        boolean success,
        boolean error,
        int numpods,
        String version,
        String datatypes,
        double timing,
        String timedout,
        double parsetiming,
        boolean parsedtimeout,
        String recalculate,
        String languagemsg,
        List<Tip> tips,
        String futuretopic,
        List<Pod> pods,
        List<Assumption> assumptions,
        List<ExamplePage> examplepages,
        List<Warning> warnings,
        List<Source> sources,
        String generalization,
        List<DidYouMean> didyoumeans,
        String error_info
) {

    public record Tip(
            String text
    ) {

    }

    public record Pod(
            String title,
            boolean error,
            int position,
            String scanner,
            String id,
            int numsubpods,
            List<Subpod> subpods
    ) {

        public record Subpod(
                String title,
                Image img,
                List<Rect> imagemap,
                String plaintext,
                String mathml,
                String sound,
                String minput,
                String moutput,
                List<Cell> cells,
                List<State> states
        ) {

            public record Image(
                    String src,
                    String alt,
                    String title,
                    int width,
                    int height,
                    String type,
                    String themes,
                    boolean colorinvertable,
                    String contenttype
            ) {

            }

            public record Rect(
                    int x,
                    int y,
                    int width,
                    int height,
                    String tooltiptext
            ) {

            }

            public record Cell(
                    String data
            ) {

            }

            public record State(
                    String name,
                    String input
            ) {

            }
        }
    }

    public record Assumption(
            String type,
            String word
    ) {

    }

    public record ExamplePage(
            String url,
            String title
    ) {

    }

    public record Warning(
            String spellcheck,
            String delimiters,
            String translation,
            String reinterpret
    ) {

    }

    public record Source(
            String name,
            String url
    ) {

    }

    public record DidYouMean(
            String didyoumean
    ) {

    }
}
