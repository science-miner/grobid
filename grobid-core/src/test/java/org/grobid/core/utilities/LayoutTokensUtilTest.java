package org.grobid.core.utilities;

import com.google.common.collect.Iterables;
import org.grobid.core.analyzers.GrobidAnalyzer;
import org.grobid.core.layout.LayoutToken;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import java.util.stream.IntStream;

import static org.junit.Assert.assertThat;

public class LayoutTokensUtilTest {


    @Test
    public void testSubList() throws Exception {

        String text = "This is a simple text that I'm making up just for fun... or well for the sake of the test!";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(text);

        layoutTokens.stream().forEach(layoutToken -> layoutToken.setOffset(layoutToken.getOffset() + 1000));

        List<LayoutToken> result = LayoutTokensUtil.subListByOffset(layoutTokens, 1005, 1008);

        assertThat(result, hasSize(2));
        assertThat(result.get(0).getText(), is("is"));
        assertThat(result.get(1).getText(), is(" "));

    }

    @Test
    public void testSubList_noEnd() throws Exception {

        String text = "This is a simple text that I'm making up just for fun... or well for the sake of the test!";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(text);

        layoutTokens.stream().forEach(layoutToken -> layoutToken.setOffset(layoutToken.getOffset() + 1000));

        List<LayoutToken> result = LayoutTokensUtil.subListByOffset(layoutTokens, 1005);

        assertThat(result, hasSize(43));
        assertThat(result.get(0).getText(), is("is"));
        assertThat(Iterables.getLast(result).getText(), is("!"));

    }
    /**
     * We fake the new line in the layout token coordinates
     */
    @Test
    public void testDoesRequireDehyphenization_shouldReturnTrue() throws Exception {
        String input = "The study of iron-based supercondu- \n" +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 11), is(true));
    }

    @Test
    public void testDoesRequireDehyphenization2_shouldReturnTrue() throws Exception {
        String input = "The study of iron-based supercondu - \n" +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 12), is(true));
    }

    @Test
    public void testDoesRequireDehyphenization_composedWords_shouldReturnFalse() throws Exception {
        String input = "The study of iron-based supercondu - \n" +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 7), is(false));
        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 24), is(false));
    }

    @Test
    public void testDoesRequireDehyphenization2_composedWords_shouldReturnFalse() throws Exception {
        String input = "The study of iron- based supercondu - \n" +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 7), is(false));
    }

    @Test
    public void testDoesRequireDehyphenization3_composedWords_shouldReturnFalse() throws Exception {
        String input = "The study of iron - based supercondu - \n" +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 8), is(false));
    }

    @Test
    public void testDoesRequireDehyphenization_usingCoordinates_shouldReturnTrue() throws Exception {
        String input = "The study of iron-based supercondu -  " +
            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";

        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);

        IntStream.range(0, 15).forEach(i -> layoutTokens.get(i).setY(10));
        IntStream.range(15, layoutTokens.size()).forEach(i -> layoutTokens.get(i).setY(30));

        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 12), is(true));
    }

//    @Test
//    public void testDoesRequireDehyphenization_withoutNewLine() throws Exception {
//        String input = "The study of iron-based supercondu -  " +
//            "ctors superconductivity in the iron-pnictide LaFeAsO 1-x F x has been expanding and has \n";
//
//        List<LayoutToken> layoutTokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(input);
//
//        IntStream.range(0, 15).forEach(i -> layoutTokens.get(i).setY(10));
//        IntStream.range(15, layoutTokens.size()).forEach(i -> layoutTokens.get(i).setY(30));
//
//        assertThat(LayoutTokensUtil.doesRequireDehypenisation(layoutTokens, 12), is(true));
//    }

}