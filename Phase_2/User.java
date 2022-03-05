package Phase_2;

public class User{
    private String userName;
    private String accountStatus;

    public User(String userName, String accountStatus){
        this.userName = userName;
        this.accountStatus = accountStatus;
    }

    public void getTransactions(){
        if(this.accountStatus.equals("Admin")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Create");
            System.out.println("Delete");
            System.out.println("Post");
            System.out.println("Search");
            System.out.println("Rent");
        }
        else if(this.accountStatus.equals("Full-Standard")){
            System.out.println("Available Transactions:");
            System.out.println("Logout");
            System.out.println("Post");
            System.out.println("Rent");
        }
        else if(this.accountStatus.equals("Rent-Standard")){
            System.out.println("Logout");
            System.out.println("Rent");
        }
        else if(this.accountStatus.equals("Post-Standard")){
            System.out.println("Logout");
            System.out.println("Post");
        }
    }

    public void create(){
        
    }

    public String toString(){
        return "Current User: "+userName + "\n" + "Account Status: "+accountStatus;
    }
}