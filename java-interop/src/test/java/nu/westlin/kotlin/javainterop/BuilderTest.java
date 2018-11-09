package nu.westlin.kotlin.javainterop;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class BuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void buildFastighet_requiredFields() {
        Fastighet fastighet = Fastighet.Builder
            .objektidentitet("uuid")
            .status("gällande")
            .build();

        assertThat(fastighet.getObjektidentitet()).isEqualTo("uuid");
        assertThat(fastighet.getObjektversion()).isEqualTo(1);
        assertThat(fastighet.getVersionGiltigFran()).isNull();
        assertThat(fastighet.getVersionGiltigTill()).isNull();
        assertThat(fastighet.getStatus()).isEqualTo("gällande");
    }

    @Test
    public void buildFastighet_noStatusShouldThrowIllegalStateException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("status krävs");

        Fastighet.Builder
            .objektidentitet("uuid")
            .build();
    }

    @Test
    public void buildGemensamhetsanlaggning_requiredFields() {
        Gemensamhetsanlaggning ga = GemensamhetsanlaggningBuilder.exempel().build();

        assertThat(ga.getObjektidentitet()).isEqualTo("foo");
        assertThat(ga.getObjektversion()).isEqualTo(1);
        assertThat(ga.getVersionGiltigFran()).isNull();
        assertThat(ga.getVersionGiltigTill()).isNull();
        assertThat(ga.getStatus()).isEqualTo("levande");
    }

    @Test
    public void buildGemensamhetsanlaggning_noStatusShouldThrowIllegalStateException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("status krävs");

        new GemensamhetsanlaggningBuilder()
            .objektidentitet("foo")
            .build();
    }

    @Test
    public void buildPagaendeArende_requiredFields() {
        PagaendeArende pa = PagaendeArendeBuilderUtil.exempel().build();

        assertThat(pa.getObjektidentitet()).isEqualTo("foo");
        assertThat(pa.getObjektversion()).isEqualTo(1);
        assertThat(pa.getVersionGiltigFran()).isNull();
        assertThat(pa.getVersionGiltigTill()).isNull();
        assertThat(pa.getArendeidentitet()).isEqualTo("abc123");
    }
}
