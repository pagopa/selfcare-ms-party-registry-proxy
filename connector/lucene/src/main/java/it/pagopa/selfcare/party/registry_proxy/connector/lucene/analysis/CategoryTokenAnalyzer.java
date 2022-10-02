package it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryTokenAnalyzer extends Analyzer {//TODO: there is no need to use a standard analyzer because the search is always "by id"

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();
        final StopFilter stopFilter = new StopFilter(tokenizer, ItalianAnalyzer.getDefaultStopSet());
        final ASCIIFoldingFilter asciiFoldingFilter = new ASCIIFoldingFilter(stopFilter);
        final NGramTokenFilter nGramTokenFilter = new NGramTokenFilter(asciiFoldingFilter, 3, 5, true);
        return new TokenStreamComponents(tokenizer, nGramTokenFilter);
    }

}
