package io.czargaurav.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.czargaurav.inbox.email.Email;
import io.czargaurav.inbox.email.EmailRepository;
import io.czargaurav.inbox.emaillist.EmailListItem;
import io.czargaurav.inbox.emaillist.EmailListItemKey;
import io.czargaurav.inbox.emaillist.EmailListItemRepository;
import io.czargaurav.inbox.folders.Folder;
import io.czargaurav.inbox.folders.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@SpringBootApplication
public class InboxApp {

	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private EmailListItemRepository emailListItemRepository;
	@Autowired
	private EmailRepository emailRepository;

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(/*DataStaxAstraProperties astraProperties*/) {
		//Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> {
            try {
                builder.withCloudSecureConnectBundle(new URL("classpath:secure-connect.zip"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        };//builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("czargaurav", "Inbox", "blue"));
		folderRepository.save(new Folder("czargaurav", "Sent", "green"));
		folderRepository.save(new Folder("czargaurav", "Important", "yellow"));

		for(int i = 0; i < 10; i++) {
			EmailListItemKey key = new EmailListItemKey();
			key.setId("czargaurav");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());
			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setSubject("Subject "+i);
			item.setTo(Arrays.asList("czargaurav", "jhon", "jack"));
			item.setUnread(true);
			emailListItemRepository.save(item);

			Email email = new Email();
			email.setId(key.getTimeUUID());
			email.setFrom("czargaurav");
			email.setSubject(item.getSubject());
			email.setBody("Body "+i);
			email.setTo(item.getTo());
			emailRepository.save(email);
		}
	}
}
