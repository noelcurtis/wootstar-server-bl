package engine.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.RatioGauge;

public class CacheHitRatio extends RatioGauge
{
    private final Counter hits;
    private final Counter calls;

    public CacheHitRatio(Counter hits, Counter calls) {
        this.hits = hits;
        this.calls = calls;
    }

    @Override
    public Ratio getRatio() {
        return Ratio.of(hits.getCount(), calls.getCount());
    }

    public Double getValue() {
        return getRatio().getValue();
    }

    public void incrementHits() {
        hits.inc();
    }

    public void incrementCalls() {
        calls.inc();
    }
}
