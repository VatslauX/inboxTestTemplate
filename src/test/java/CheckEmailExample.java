import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
/**
 * Created by Viachaslau Balashevich.
 * https://www.linkedin.com/in/viachaslau
 */

// TODO All logic and constants are here to simplify the example
public class CheckEmailExample {
    public static final String INBOX_URL = "https://www.google.com/inbox/";
    public static final SelenideElement INBOX_SIGNIN = $(byXpath("//a[contains(text(),'Sign in')]"));
    public static final SelenideElement INBOX_BTN_DONE = $(byXpath("//*[@title='Mark done']"));
    public static final SelenideElement INBOX_BTN_INBOX = $(byXpath("//nav[@id]/div/ul//span[contains(.,'Inbox')]"));
    public static final SelenideElement INBOX_BTN_DELETE = $(byXpath("//div[@jsaction='global.none']/ul/li[3][@role='button' and @title='Delete']"));
    public static final SelenideElement INBOX_MENU_REPLY = $(byXpath("//div[@role='button'][@title='Reply, Forward & more']"));
    public static final SelenideElement INBOX_MENU_SHOW_ORIGINAL = $(byXpath("//span[@class='do'][@title='Show original']"));
    public static final SelenideElement INBOX_BTN_TRASH = $(byXpath("//span[@title='Trash']"));
    public static final SelenideElement INBOX_BTN_EMPTY_TRASH = $(byXpath("//button[contains(.,'EMPTY TRASH NOW')]"));
    public static final SelenideElement INBOX_CONFIRM_EMPTY_TRASH = $(byXpath("//div[@role='dialog']//div/div[contains(.,'OK')]"));
    public static final SelenideElement GMAIL_LOGIN_COOKIE = $(byName("PersistentCookie"));
    public static final SelenideElement GMAIL_LOGIN_FIELD = $(byName("Email"));
    public static final SelenideElement GMAIL_PASSWORD_FIELD = $(byId("Passwd"));
    public static final SelenideElement GMAIL_SIGNIN_BTN = $(byId("signIn"));
    public static final SelenideElement GMAIL_NEXT_BTN = $(byId("next"));
    public static final SelenideElement GMAIL_INBOX_BTN = $(byXpath("//div[@role='navigation']"));
    public static final SelenideElement GMAIL_SELECT_MENU = $(byXpath("//*[@data-tooltip='Select']/div[1]/div"));
    public static final SelenideElement GMAIL_ARCHIVE_BTN = $(byXpath("//*[@data-tooltip='Archive']"));


    public static void signInGmailInbox(String GMAIL_LOGIN, String GMAIL_PASSWORD) { //Logic for open INBOX twice or more times in one session without logout
        System.out.println("Login GoogleInbox");
        open(INBOX_URL);
        sleep(2000);
        INBOX_SIGNIN.waitUntil(visible, 15000).click();
        sleep(3000);
        if(GMAIL_LOGIN_FIELD.is(visible) == true) {
            System.out.println("Type email");
            GMAIL_LOGIN_FIELD.sendKeys(GMAIL_LOGIN);
            System.out.println("Submit email");
            GMAIL_NEXT_BTN.click();
            System.out.println("Type password");
            GMAIL_PASSWORD_FIELD.shouldBe(visible).sendKeys(GMAIL_PASSWORD);
            if(GMAIL_LOGIN_COOKIE.isSelected()){
                GMAIL_LOGIN_COOKIE.setSelected(false);
                GMAIL_LOGIN_COOKIE.shouldNotBe(selected);
            }
            System.out.println("Submit password");
            GMAIL_SIGNIN_BTN.shouldBe(visible).click();
            System.out.println("Inbox opened");
        }
        if (GMAIL_PASSWORD_FIELD.is(visible) == true){
            System.out.println("Type password");
            GMAIL_PASSWORD_FIELD.shouldBe(visible).sendKeys(GMAIL_PASSWORD);
            System.out.println("Submit password");
            GMAIL_SIGNIN_BTN.shouldBe(visible).click();
            System.out.println("Inbox opened");
        }
        else {
            System.out.println("Inbox opened");
        }
        if (INBOX_BTN_TRASH.is(visible) == true){
            System.out.println("User is logged in and inbox is opened");
        }
    }
    public static void detectEmail(SelenideElement EMAIL_SUBJECT){
        System.out.println("Detect email");
        if (EMAIL_SUBJECT.exists() == false) {
            int count = 0;
            do {
                sleep(20000);
                refresh();
                count++;
                System.out.println("Email by subject NOT found loop" + count);
                if (EMAIL_SUBJECT.exists() == true) {
                    break;
                }
            } while (count < 10);
        }
        if (EMAIL_SUBJECT.exists() == false) {
            Assert.fail("Email not found - " + EMAIL_SUBJECT);
        }

    }
    public static void openEmail(SelenideElement EMAIL_SUBJECT){
        System.out.println("Open email");
        sleep(4000);
        if (EMAIL_SUBJECT.exists() == false) {
            Assert.fail("Subject email not found - " + EMAIL_SUBJECT);
        }
        if (EMAIL_SUBJECT.exists() == true) {
            EMAIL_SUBJECT.waitUntil(visible, 15000).click();
            System.out.println("Email by subject found");
            sleep(1500);
            INBOX_BTN_DELETE.waitUntil(visible, 10000);
            System.out.println("Email opened");
        }
    }
    public static void checkEmailTitle(String EMAIL_TITLE){
        System.out.println("Check title");
        if (EMAIL_TITLE == null) {
            Assert.fail("Title email is - " + EMAIL_TITLE);
        }
        $$(byText(EMAIL_TITLE)).filter(visible);
        System.out.println(EMAIL_TITLE + "- email present");
    }
    public static void checkEmailText(String EMAIL_TEXT){
        System.out.println("Check email Text");
        if (EMAIL_TEXT == null) {
            Assert.fail("Title email is - " + EMAIL_TEXT);
        }
        $$(byText(EMAIL_TEXT)).filter(visible);
        System.out.println(EMAIL_TEXT + " - email present");
    }
    public static void deleteEmail() {
        System.out.println("Delete email");
        sleep(1000);
        INBOX_BTN_DELETE.waitUntil(visible, 20000).click();
        INBOX_BTN_DELETE.waitUntil(not(visible), 10000);
    }
    public static void inboxEmptyTrash(){
        System.out.println("Empty trash");
        sleep(1000);
        INBOX_BTN_TRASH.waitUntil(visible, 10000).click();
        sleep(1000);
        INBOX_BTN_EMPTY_TRASH.waitUntil(visible, 10000).click();
        sleep(1000);
        INBOX_CONFIRM_EMPTY_TRASH.waitUntil(visible, 10000).click();
        sleep(1000);
        $(byText("Nothing in Trash")).waitUntil(visible, 10000);
        System.out.println("Trash cleared");
        if ($(byText("Nothing in Trash")) == null) {
            Assert.fail("Trash NOT cleared");
        }
    }
    public static void logoutGoogleInbox(){
        System.out.println("Logout Google");
        open("https://accounts.google.com/SignOutOptions?hl=en&continue=https://inbox.google.com/%3Fcid%3Dimp");
        if ($(byId("signout")).isDisplayed()) {
            $(byId("signout")).click();
            sleep(3000);
            checkText("Sign in with your Google Account");
        }
    }
    public static boolean checkText(String textString) {
        $(byText(textString)).waitUntil(exist, 20000);
        $$(byText(textString)).filter(visible).shouldHaveSize(1);
        return true;
    }
    @Rule
    public Timeout tests = Timeout.seconds(600);
    @Test
    public void checkEmail(){
        //TODO here is test data please fill own credentials and stuff
        String emailSubject = "";
        SelenideElement EMAIL_SUBJECT = $(byXpath("//span[contains(.,'"+emailSubject+"')]"));
        String GMAIL_LOGIN = "";
        String GMAIL_PASSWORD = "";
        String EMAIL_TITLE = "";
        String EMAIL_TEXT = "";
        //TODO Google is against GUI automation - so as percondition You need to open Inbox from same IP range to avoid security warnings

        try {
            signInGmailInbox(GMAIL_LOGIN, GMAIL_PASSWORD);
            detectEmail(EMAIL_SUBJECT);
            openEmail(EMAIL_SUBJECT);
            //TODO - here are different check methods - You can extend checks via adding new methods
            checkEmailTitle(EMAIL_TITLE);
            checkEmailText(EMAIL_TEXT);
            //TODO - optional steps: is needed in some cases, because google merge HTML letters in the stack if the are in the same thread
            deleteEmail();
            inboxEmptyTrash();
        }
        //TODO - optional step: is needed if You want open app with different users in same browser session
        finally {
            logoutGoogleInbox();
        }

    }
}
