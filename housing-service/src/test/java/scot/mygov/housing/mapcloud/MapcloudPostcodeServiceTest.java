package scot.mygov.housing.mapcloud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

public class MapcloudPostcodeServiceTest {
//
//    @Test(expected = PostcodeServiceException.class)
//    public void processingExceptionHandledCorrectly() throws PostcodeServiceException {
//
//        // ARRANGE
//        PostcodeService sut = serviceWithTarget(serverErrorTarget(new ProcessingException("processingEx")));
//
//        // ACT
//        PostcodeServiceResults actual = sut.lookup(scottishPostcode());
//
//        // ASSERT -- see expected exception
//    }
//
//    @Test(expected = PostcodeServiceException.class)
//    public void webAppExceptionHandledCorrectly() throws PostcodeServiceException {
//
//        // ARRANGE
//        PostcodeService sut = serviceWithTarget(serverErrorTarget(new WebApplicationException("processingEx")));
//
//        // ACT
//        PostcodeServiceResults actual = sut.lookup(scottishPostcode());
//
//        // ASSERT -- see expected exception
//    }
//
//    @Test
//    public void greenpathResultsParsedCorrectly() throws PostcodeServiceException {
//
//        // ARRANGE
//        MapcloudResults results = greenpathMapcloudResults();
//        PostcodeService sut = serviceWithTarget(greenpathTarget(results));
//
//        // ACT
//        PostcodeServiceResults actual = sut.lookup(scottishPostcode());
//
//        // ASSERT
//        assertFalse(actual.getResults().isEmpty());
//
//        PostcodeServiceResult result1 = actual.getResults().get(0);
//        assertEquals(result1.getPostcode(), results.getResults().get(0).getPostcode(), scottishPostcode());
//
//        List<String> expectedAddressLines = new ArrayList<>();
//        Collections.addAll(expectedAddressLines,
//                results.getResults().get(0).getAddressLine1(),
//                results.getResults().get(0).getAddressLine2(),
//                results.getResults().get(0).getAddressLine3());
//        assertEquals(expectedAddressLines, result1.getAddressLines());
//        assertEquals(results.getResults().get(0).getUprn(), result1.getUprn());
//        assertEquals(results.getResults().get(0).getTown(), result1.getTown());
//    }
//
//    @Test
//    public void emptyAddressLinesAreOmitted() throws PostcodeServiceException {
//
//        // ARRANGE
//        MapcloudResults results = greenpathMapcloudResults();
//        // address line 2 should be missing from the results
//        results.getResults().get(0).setAddressLine2("");
//        PostcodeService sut = serviceWithTarget(greenpathTarget(results));
//
//        // ACT
//        PostcodeServiceResults actual = sut.lookup(scottishPostcode());
//
//        // ASSERT
//        assertFalse(actual.getResults().isEmpty());
//
//        PostcodeServiceResult result1 = actual.getResults().get(0);
//        List<String> expectedAddressLines = new ArrayList<>();
//        Collections.addAll(expectedAddressLines,
//                results.getResults().get(0).getAddressLine1(),
//                results.getResults().get(0).getAddressLine3());
//        assertEquals(expectedAddressLines, result1.getAddressLines());
//    }
//
//    @Test
//    public void successMetricsCountedCorrectly() throws PostcodeServiceException {
//        // ARRANGE
//        MetricRegistry registry = new MetricRegistry();
//        MapcloudResults results = greenpathMapcloudResults();
//        PostcodeService sut = serviceWithTargetAndRegistry(greenpathTarget(results), registry);
//
//        // ACT
//        sut.lookup(scottishPostcode());
//
//        // ASSERT
//        assertEquals(registry.getCounters().get(MetricName.REQUESTS.name(sut)).getCount(), 1);
//        assertEquals(registry.getMeters().get(MetricName.REQUEST_RATE.name(sut)).getCount(), 1);
//        assertEquals(registry.getCounters().get(MetricName.ERRORS.name(sut)).getCount(), 0);
//        assertEquals(registry.getMeters().get(MetricName.ERROR_RATE.name(sut)).getCount(), 0);
//    }
//
//    @Test
//    public void errorMetricsCountedCorrectly() throws PostcodeServiceException {
//        // ARRANGE
//        MetricRegistry registry = new MetricRegistry();
//        MapcloudResults results = greenpathMapcloudResults();
//        PostcodeService sut = serviceWithTargetAndRegistry(
//                serverErrorTarget(new WebApplicationException("processingEx")),
//                registry);
//
//        // ACT
//        try {
//            sut.lookup(scottishPostcode());
//        } catch (PostcodeServiceException e) {
//        }
//
//
//        // ASSERT
//        assertEquals(registry.getCounters().get(MetricName.REQUESTS.name(sut)).getCount(), 1);
//        assertEquals(registry.getMeters().get(MetricName.REQUEST_RATE.name(sut)).getCount(), 1);
//        assertEquals(registry.getCounters().get(MetricName.ERRORS.name(sut)).getCount(), 1);
//        assertEquals(registry.getMeters().get(MetricName.ERROR_RATE.name(sut)).getCount(), 1);
//    }
//
//    private MapcloudPostcodeService serviceWithTarget(WebTarget target) {
//        return serviceWithTargetAndRegistry(target, anyRegistry());
//    }
//
//    private MapcloudPostcodeService serviceWithTargetAndRegistry(WebTarget target, MetricRegistry registry) {
//        return new MapcloudPostcodeService(target, "anyUser", "anyPassword", registry);
//    }
//
//    private String scottishPostcode() {
//        return "EH104AX";
//    }
//
//    private WebTarget greenpathTarget(MapcloudResults results) {
//        WebTarget target = mock(WebTarget.class);
//        Invocation.Builder builder = mock(Invocation.Builder.class);
//        when(target.path(any())).thenReturn(target);
//        when(target.queryParam(any(), any())).thenReturn(target);
//        when(target.request()).thenReturn(builder);
//        when(builder.header(any(), any())).thenReturn(builder);
//        when(builder.get(eq(MapcloudResults.class))).thenReturn(results);
//        return target;
//    }
//
//    private WebTarget serverErrorTarget(RuntimeException ex) {
//        WebTarget target = mock(WebTarget.class);
//        Invocation.Builder builder = mock(Invocation.Builder.class);
//        when(target.path(any())).thenReturn(target);
//        when(target.queryParam(any(), any())).thenReturn(target);
//        when(target.request()).thenReturn(builder);
//        when(builder.header(any(), any())).thenReturn(builder);
//        when(builder.get(eq(MapcloudResults.class))).thenThrow(ex);
//        return target;
//    }
//
//    private MapcloudResults greenpathMapcloudResults() {
//        MapcloudResults res = new MapcloudResults();
//        List<MapcloudResult> resultsList = new ArrayList<>();
//        resultsList.add(anyResult());
//        res.setResults(resultsList);
//        return res;
//    }
//
//    private MapcloudResult anyResult() {
//        MapcloudResult res = new MapcloudResult();
//        res.setPostcode(scottishPostcode());
//        res.setUprn("1111");
//        res.setAddressLine1("line1");
//        res.setAddressLine2("line2");
//        res.setAddressLine3("line3");
//        res.setTown("town");
//        return res;
//    }
//
//    private MetricRegistry anyRegistry() {
//        return new MetricRegistry();
//    }

}
