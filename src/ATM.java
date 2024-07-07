import java.util.Scanner;

public class ATM {
    private static final Scanner scanner;
    private static final ATMService atmService;

    public ATM() {
    }

    public static void main(String[] args) {
        while(true) {
            System.out.println("Введите номер карты (XXXX-XXXX-XXXX-XXXX): ");
            String cardNumber = scanner.nextLine();
            if (!cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
                System.out.println("Номер карты не соответствует формату! Попробуйте снова.");
                continue;
            }

            Card card = atmService.findCard(cardNumber);
            if (card == null) {
                System.out.println("Такой карты нет!");
            } else {
                atmService.checkCardUnblock(card);
                boolean authenticated = false;

                while(!authenticated) {
                    System.out.println("Введите ПИН-код: ");
                    String pinCode = scanner.nextLine();
                    if (atmService.authenticate(card, pinCode)) {
                        authenticated = true;
                        System.out.println("Авторизация успешна.");
                        showMenu();
                    } else {
                        System.out.println("Неверный ПИН-код. Повторить? (да/нет)");
                        String retry = scanner.nextLine();
                        if (retry.equalsIgnoreCase("нет")) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void showMenu() {
        while(true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Проверить баланс");
            System.out.println("2. Снять средства");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. Выйти");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Текущий баланс: " + atmService.checkBalance());
                    break;
                case 2:
                    System.out.println("Введите сумму для снятия: ");
                    double amountToWithdraw = Double.parseDouble(scanner.nextLine());
                    if (atmService.withdraw(amountToWithdraw)) {
                        System.out.println("Средства успешно сняты.");
                        break;
                    }

                    System.out.println("Ошибка. Недостаточно средств.");
                    break;
                case 3:
                    System.out.println("Введите сумму для пополнения: ");
                    double amountToDeposit = Double.parseDouble(scanner.nextLine());
                    if (atmService.deposit(amountToDeposit)) {
                        System.out.println("Баланс успешно пополнен.");
                        break;
                    }

                    System.out.println("Ошибка. Сумма пополнения превышает лимит.");
                    break;
                case 4:
                    atmService.logout();
                    System.out.println("Выход из системы.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    static {
        scanner = new Scanner(System.in);
        atmService = new ATMService();
    }
}
