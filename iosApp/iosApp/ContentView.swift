import UIKit
import CarBode
import AVFoundation
import SwiftUI
import shared
import CodeScanner

struct ComposeView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        return MainKt.MainViewController(
            scannerUIViewController: { onSuccess, onFailed, onCanceled in
                ScannerViewController(
                    onSuccess: { result in
                        onSuccess(result)
                    },
                    onFailed: { exception in
                        onFailed(exception)
                    },
                    onCanceled: {
                        onCanceled()
                    }
                )
            }
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all)
    }
}


class ScannerViewController: UIViewController {
    
    var onSuccess: (String) -> Void
    var onFailed: (KotlinException) -> Void
    var onCanceled: () -> Void
    
    init(
        onSuccess: @escaping (String) -> Void,
        onFailed: @escaping (KotlinException) -> Void,
        onCanceled: @escaping () -> Void
    ) {
        self.onSuccess = onSuccess
        self.onFailed = onFailed
        self.onCanceled = onCanceled
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpBarcode()
    }
    
    private func setUpBarcode(){
        
        guard let device =  AVCaptureDevice.default(for: .video) else { return }
        
        let codeScannerView = ZStack {
            CodeScannerView(
                codeTypes: provideBarcodeOptions(),
                videoCaptureDevice: device
            ) { response in
                switch response {
                case .success(let result):
                    self.onSuccess(result.string)
                case .failure(let error):
                    print(error.localizedDescription)
                }
            }
            
            VStack {
                ScannerTopBar(onCanceled: onCanceled, device: device)
                Spacer()
            }
        }
        
        layout(codeScannerView: codeScannerView)
    }
    
    private func provideBarcodeOptions() -> [AVMetadataObject.ObjectType] {
        return [
            .code128,
            .ean13,
            .ean8,
            .upce
        ]
    }
    
    private func layout(codeScannerView: some View){
        let hostingController = UIHostingController(rootView: codeScannerView)
        addChild(hostingController)
        view.addSubview(hostingController.view)
        
        hostingController.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            hostingController.view.topAnchor.constraint(equalTo: view.topAnchor),
            hostingController.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            hostingController.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            hostingController.view.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])
        
        hostingController.didMove(toParent: self)
    }
    
}

struct ScannerTopBar : View {
    
    var onCanceled: () -> Void
    @State var isTorchOn: Bool = false
    var device: AVCaptureDevice
    
    var body: some View {
        HStack {
            Button(action: {
                self.onCanceled()
            }) {
                Image(systemName: "xmark")
                    .resizable()
                    .foregroundStyle(.black)
                    .padding(10)
            }
            .frame(width: 35, height: 35)
            .background(Color.white)
            .cornerRadius(20)
            .padding([.top, .leading], 20)
            Spacer()
            Button(action: {
                self.isTorchOn.toggle()
                print(self.isTorchOn)
                toggleTorch(on: self.isTorchOn, device: device)
            }) {
                Image(systemName: isTorchOn ? "bolt.fill" : "bolt.slash")
                    .resizable()
                    .foregroundStyle(.black)
                    .padding(10)
            }
            .frame(width: 35, height: 35)
            .background(Color.white)
            .cornerRadius(20)
            .padding([.top, .trailing], 20)
        }
    }
}

func toggleTorch(on: Bool, device: AVCaptureDevice) {
    if device.hasTorch {
        do {
            try device.lockForConfiguration()
            
            if on == true {
                device.torchMode = .on
            } else {
                device.torchMode = .off
            }
            
            device.unlockForConfiguration()
        } catch {
            print("Torch could not be used")
        }
    } else {
        print("Torch is not available")
    }
}

