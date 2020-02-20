package cn.edu.hebut.easydesign.DataManagement;

import java.io.InputStream;

public interface DataLoader {
    <D extends Data> D LoadFromNet(String r, long id, Object... extraArgs) throws Exception;
    <D extends Data> D LoadFromCache(InputStream stream, long id) throws Exception;
}
