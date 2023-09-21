package it.pagopa.selfcare.party.connector.azure_storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

class AzureBlobClientTest {

    @Test
    void getFileOk() throws URISyntaxException, IOException, NoSuchFieldException, IllegalAccessException, StorageException, InvalidKeyException {
        //given
        AzureBlobClient blobClient = new AzureBlobClient("UseDevelopmentStorage=true;", "$web");
        CloudBlockBlob blockBlobMock = Mockito.mock(CloudBlockBlob.class);
        Mockito.when(blockBlobMock.getProperties())
                .thenReturn(new BlobProperties());
        Mockito.doNothing().
                when(blockBlobMock).upload(Mockito.any(), Mockito.anyByte());
        CloudBlobContainer blobContainerMock = Mockito.mock(CloudBlobContainer.class);
        Mockito.when(blobContainerMock.getBlockBlobReference(Mockito.anyString()))
                .thenReturn(blockBlobMock);
        CloudBlobClient blobClientMock = Mockito.mock(CloudBlobClient.class);
        Mockito.when(blobClientMock.getContainerReference("$web"))
                .thenReturn(blobContainerMock);
        mockCloudBlobClient(blobClient, blobClientMock);

        Executable executable = () -> blobClient.getFile("logo-pagopa-spa.png");
        // then
        Assertions.assertDoesNotThrow(executable);
    }

    private void mockCloudBlobClient(AzureBlobClient blobClient, CloudBlobClient blobClientMock) throws NoSuchFieldException, IllegalAccessException {
        Field field = AzureBlobClient.class.getDeclaredField("blobClient");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(blobClient, blobClientMock);
    }

}
