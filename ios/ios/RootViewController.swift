import common

class RootViewController: DelegatedUIViewController {
    override func viewDidLoad() {
        setDelegate(delegate: RootViewControllerDelegate())
        super.viewDidLoad()
    }
}
