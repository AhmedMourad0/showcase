import UIKit
import common

class DelegatedUIViewController : UIViewController {
    
    var delegate: UIViewControllerDelegate!
    
    func setDelegate(delegate: UIViewControllerDelegate) {
        self.delegate = delegate
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        delegate.onViewDidLoad(self: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        delegate.onViewWillAppear(self: self)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        delegate.onViewWillDisappear(self: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        delegate.prepareForSegue(self: self, segue: segue, sender: sender)
    }
    
    override func willMove(toParent parent: UIViewController?) {
        super.willMove(toParent: parent)
        delegate.willMove(to: parent)
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return UIStatusBarStyle(rawValue: Int(delegate.statusBarStyle))!
    }
}
